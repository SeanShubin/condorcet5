select tally.id,
       election.name  election,
       candidate.name candidate,
       tally.`rank`,
       tally.election_id,
       tally.candidate_id
from tally
         inner join election on tally.election_id = election.id
         inner join candidate on tally.candidate_id = candidate.id