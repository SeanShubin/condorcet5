select election.id,
       user.name     owner,
       election.name name,
       status.name   status,
       secret,
       end,
       election.owner_id,
       status_id
from election
         inner join user on election.owner_id = user.id
         inner join status on election.status_id = status.id
