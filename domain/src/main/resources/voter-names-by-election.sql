select user.name
from voter
         inner join user
                    on voter.user_id = user.id
         inner join election
                    on voter.election_id = election.id
where election.name = ?
