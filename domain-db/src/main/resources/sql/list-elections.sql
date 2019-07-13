select user.name     owner,
       election.name name,
       end,
       secret,
       status.name   status
from election
         inner join user on election.owner_id = user.id
         inner join status on election.status_id = status.id
order by election.id
