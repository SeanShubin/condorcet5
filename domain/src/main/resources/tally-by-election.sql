select election.name  election,
       candidate.name candidate,
       `rank`
from tally
         inner join election
                    on tally.election_id = election.id
         inner join candidate
                    on tally.candidate_id = candidate.id
where election.name = ?
