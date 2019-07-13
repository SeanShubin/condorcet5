select election.name election,
       tally.report
from tally
         inner join election
                    on tally.election_id = election.id
where election.name = ?
