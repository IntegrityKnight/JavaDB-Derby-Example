CALL SYSCS_UTIL.SYSCS_EXPORT_QUERY_LOBS_TO_EXTFILE
(
'SELECT DETAILS_2, MEDIA_2, MEDIA_2_FILENAME 
 FROM acg_character.profile 
 WHERE ID=2', 
'A:\javatoybox\test_data\Details_¸Ô²Ó.txt', 
'|', 
'"', 
'UTF-8', 
'A:\javatoybox\test_data\Bazelgeuse_BGM.mp3'
);