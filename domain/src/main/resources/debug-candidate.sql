select candidate.id,
       election.name election,
       candidate.name,
       candidate.election_id
from candidate
         inner join election on candidate.election_id = election.id
