select voter.id,
       user.name     voter,
       election.name election,
       voter.user_id,
       voter.election_id
from voter
         inner join election on voter.election_id = election.id
         inner join user on voter.user_id = user.id
order by voter.id
