select user.name      voter,
       election.name  election,
       candidate.name candidate,
       ranking.rank   `rank`
from ranking
         inner join candidate
                    on ranking.candidate_id = candidate.id
         inner join ballot
                    on ranking.ballot_id = ballot.id
         inner join user
                    on ballot.user_id = user.id
         inner join election
                    on ballot.election_id = election.id
where user.name = ?
  and election.name = ?
