select user.name   as owner,
       election.name,
       election.end,
       election.secret,
       status.name as status
from election
         inner join user
                    on election.owner_id = user.id
         inner join status
                    on election.status_id = status.id
where election.name = ?
