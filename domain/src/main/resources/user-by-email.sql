select name,
       email,
       salt,
       hash
from user
where email = ?
