select id `when`, source, type, owner, text
from event
where id > ?
order by id
