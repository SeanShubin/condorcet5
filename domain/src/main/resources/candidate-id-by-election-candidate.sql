select candidate.id
from candidate
         inner join election
                    on candidate.election_id = election.id
where election.name = ?
  and candidate.name = ?
