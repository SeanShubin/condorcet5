select event.id,
       event.`when`,
       event.owner,
       event.type,
       event.text
from event
order by event.id
