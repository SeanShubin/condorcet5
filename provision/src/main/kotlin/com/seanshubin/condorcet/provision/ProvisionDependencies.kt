package com.seanshubin.condorcet.provision

import com.amazonaws.regions.Regions
import com.amazonaws.services.cloudformation.AmazonCloudFormation
import com.amazonaws.services.cloudformation.AmazonCloudFormationClientBuilder
import com.seanshubin.condorcet.contract.SystemContract
import com.seanshubin.condorcet.contract.SystemDelegate
import com.seanshubin.condorcet.domain.db.Initializer
import com.seanshubin.condorcet.domain.db.SchemaInitializerFactory
import com.seanshubin.condorcet.logger.LogDecorators
import com.seanshubin.condorcet.logger.LogGroup
import com.seanshubin.condorcet.logger.Logger
import com.seanshubin.condorcet.logger.LoggerFactory
import com.seanshubin.condorcet.retry.Retry
import com.seanshubin.condorcet.retry.RetryWithCoroutines
import com.seanshubin.condorcet.util.ClassLoaderUtil
import com.seanshubin.condorcet.util.db.ConnectionFactory
import com.seanshubin.condorcet.util.db.ConnectionWrapper
import java.nio.file.Path
import java.nio.file.Paths

class ProvisionDependencies {
    private val logDir: Path = Paths.get("out", "log", "provision")
    private val logGroup: LogGroup = LoggerFactory.instanceDefaultZone.createLogGroup(logDir)
    private val sqlLogger: Logger = logGroup.create("sql")
    private val stackStatusLogger: Logger = logGroup.create("stack-status")
    private val sqlEvent: (String) -> Unit = LogDecorators.logSql(sqlLogger)
    private val stackStatusEvent: (String) -> Unit = stackStatusLogger::log
    private val host: String = "localhost"
    private val user: String = "root"
    private val password: String = "insecure"
    private val schemaName: String = "condorcet"
    private val connection: ConnectionWrapper = ConnectionFactory.createConnection(host, user, password, sqlEvent)
    private val initializer: Initializer = SchemaInitializerFactory.create(connection, schemaName)
    private val cloudFormationBuilder: AmazonCloudFormationClientBuilder = AmazonCloudFormationClientBuilder.standard()
    private val awsRegion = Regions.US_WEST_1
    private val cloudFormation: AmazonCloudFormation = cloudFormationBuilder.withRegion(awsRegion).build()
    private val simplifiedCloudFormation: SimplifiedCloudFormation = SimplifiedCloudFormationAws(cloudFormation)
    private val stackName: String = "condorcet-stack"
    private val stackResource: String = "condorcet-stack.json"
    private val loadResource: (String) -> String = ClassLoaderUtil::loadResourceAsString
    private val system: SystemContract = SystemDelegate
    private val retry: Retry = RetryWithCoroutines(system)
    private val deployer: Deployer = CloudFormationDeployer(
            simplifiedCloudFormation, stackName, stackResource, loadResource, retry, stackStatusEvent)
    val provisionSetup: Runnable = ProvisionSetup(deployer, initializer)
    val provisionTeardown: Runnable = ProvisionTeardown(deployer)
}
