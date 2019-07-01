select candidate.name
from candidate
         inner join election
                    on candidate.election_id = election.id
where election.name = ?
