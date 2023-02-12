SELECT st.name,
       st.age,
       f.name
FROM student AS st
         LEFT JOIN faculty AS f ON st.faculty_id = f.id;

SELECT st.name,
       st.age
FROM student AS st
         INNER JOIN avatar AS av ON st.avatar_id = av.id;

