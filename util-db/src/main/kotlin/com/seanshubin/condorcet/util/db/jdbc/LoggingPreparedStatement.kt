package com.seanshubin.condorcet.util.db.jdbc

import com.mysql.cj.jdbc.ClientPreparedStatement
import java.io.InputStream
import java.io.Reader
import java.math.BigDecimal
import java.net.URL
import java.sql.*
import java.sql.Date
import java.util.*

class LoggingPreparedStatement(
        private val sql: String,
        private val preparedStatement: PreparedStatement,
        private val emitLine: (String) -> Unit) : PreparedStatement {
    override fun setRef(parameterIndex: Int, x: Ref?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setBlob(parameterIndex: Int, x: Blob?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setBlob(parameterIndex: Int, inputStream: InputStream?, length: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setBlob(parameterIndex: Int, inputStream: InputStream?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setCharacterStream(parameterIndex: Int, reader: Reader?, length: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setCharacterStream(parameterIndex: Int, reader: Reader?, length: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setCharacterStream(parameterIndex: Int, reader: Reader?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setArray(parameterIndex: Int, x: java.sql.Array?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getResultSetType(): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setDate(parameterIndex: Int, x: Date?) {
        preparedStatement.setDate(parameterIndex, x)
    }

    override fun setDate(parameterIndex: Int, x: Date?, cal: Calendar?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun clearParameters() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun cancel() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getConnection(): Connection {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setObject(parameterIndex: Int, x: Any?, targetSqlType: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setObject(parameterIndex: Int, x: Any?) {
        preparedStatement.setObject(parameterIndex, x)
    }

    override fun setObject(parameterIndex: Int, x: Any?, targetSqlType: Int, scaleOrLength: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setMaxFieldSize(max: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setBytes(parameterIndex: Int, x: ByteArray?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setLong(parameterIndex: Int, x: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setClob(parameterIndex: Int, x: Clob?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setClob(parameterIndex: Int, reader: Reader?, length: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setClob(parameterIndex: Int, reader: Reader?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getWarnings(): SQLWarning {
        throw UnsupportedOperationException("not implemented")
    }

    override fun executeQuery(): ResultSet {
        emitLine(preparedStatement.asSql())
        return preparedStatement.executeQuery()
    }

    override fun executeQuery(sql: String?): ResultSet {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setUnicodeStream(parameterIndex: Int, x: InputStream?, length: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun close() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun isClosed(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setNString(parameterIndex: Int, value: String?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getMaxFieldSize(): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setURL(parameterIndex: Int, x: URL?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getUpdateCount(): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setRowId(parameterIndex: Int, x: RowId?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setFloat(parameterIndex: Int, x: Float) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setFetchDirection(direction: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getFetchSize(): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setTime(parameterIndex: Int, x: Time?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setTime(parameterIndex: Int, x: Time?, cal: Calendar?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun executeBatch(): IntArray {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getQueryTimeout(): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun isPoolable(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setBinaryStream(parameterIndex: Int, x: InputStream?, length: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setBinaryStream(parameterIndex: Int, x: InputStream?, length: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setBinaryStream(parameterIndex: Int, x: InputStream?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setNCharacterStream(parameterIndex: Int, value: Reader?, length: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setNCharacterStream(parameterIndex: Int, value: Reader?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setInt(parameterIndex: Int, x: Int) {
        preparedStatement.setInt(parameterIndex, x)
    }

    override fun getGeneratedKeys(): ResultSet {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getResultSetConcurrency(): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getResultSet(): ResultSet {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setDouble(parameterIndex: Int, x: Double) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun closeOnCompletion() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getParameterMetaData(): ParameterMetaData {
        throw UnsupportedOperationException("not implemented")
    }

    override fun executeUpdate(): Int {
        emitLine(preparedStatement.asSql())
        return preparedStatement.executeUpdate()
    }

    override fun executeUpdate(sql: String?): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun executeUpdate(sql: String?, autoGeneratedKeys: Int): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun executeUpdate(sql: String?, columnIndexes: IntArray?): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun executeUpdate(sql: String?, columnNames: Array<out String>?): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun clearBatch() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun isCloseOnCompletion(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun <T : Any?> unwrap(iface: Class<T>?): T {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getMaxRows(): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setSQLXML(parameterIndex: Int, xmlObject: SQLXML?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setBigDecimal(parameterIndex: Int, x: BigDecimal?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setString(parameterIndex: Int, x: String?) {
        preparedStatement.setString(parameterIndex, x)
    }

    override fun setAsciiStream(parameterIndex: Int, x: InputStream?, length: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setAsciiStream(parameterIndex: Int, x: InputStream?, length: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setAsciiStream(parameterIndex: Int, x: InputStream?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setNClob(parameterIndex: Int, value: NClob?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setNClob(parameterIndex: Int, reader: Reader?, length: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setNClob(parameterIndex: Int, reader: Reader?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun isWrapperFor(iface: Class<*>?): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setNull(parameterIndex: Int, sqlType: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setNull(parameterIndex: Int, sqlType: Int, typeName: String?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setMaxRows(max: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setTimestamp(parameterIndex: Int, x: Timestamp?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setTimestamp(parameterIndex: Int, x: Timestamp?, cal: Calendar?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setEscapeProcessing(enable: Boolean) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setCursorName(name: String?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun execute(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun execute(sql: String?): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun execute(sql: String?, autoGeneratedKeys: Int): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun execute(sql: String?, columnIndexes: IntArray?): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun execute(sql: String?, columnNames: Array<out String>?): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setPoolable(poolable: Boolean) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setShort(parameterIndex: Int, x: Short) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setFetchSize(rows: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun clearWarnings() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getMetaData(): ResultSetMetaData {
        throw UnsupportedOperationException("not implemented")
    }

    override fun addBatch() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun addBatch(sql: String?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setQueryTimeout(seconds: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getFetchDirection(): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getResultSetHoldability(): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setBoolean(parameterIndex: Int, x: Boolean) {
        preparedStatement.setBoolean(parameterIndex, x)
    }

    override fun getMoreResults(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getMoreResults(current: Int): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setByte(parameterIndex: Int, x: Byte) {
        throw UnsupportedOperationException("not implemented")
    }

    private fun PreparedStatement.asSql(): String =
            (this as ClientPreparedStatement).asSql() + ";"
}
