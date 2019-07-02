select ranking.id,
       election.name  election,
       user.name      voter,
       candidate.name candidate,
       ranking.rank,
       ballot.confirmation,
       ballot.when_cast,
       ballot.election_id,
       ranking.ballot_id,
       ranking.candidate_id,
       ballot.user_id
from ranking
         inner join ballot on ranking.ballot_id = ballot.id
         inner join election on ballot.election_id = election.id
         inner join candidate on ranking.candidate_id = candidate.id
         inner join user on ballot.user_id = user.id
order by election.name,
         user.name,
         ranking.rank
