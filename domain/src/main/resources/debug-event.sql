select event.id,
       event.`when`,
       user.name owner,
       event.type,
       event.text,
       event.owner_id
from event
         inner join user on event.owner_id = user.id
order by event.id
