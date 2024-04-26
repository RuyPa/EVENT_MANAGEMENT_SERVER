use register;

-- DROP TEMPORARY TABLE IF EXISTS slotdadangki;

--     SELECT a.tuan_id, a.ngay_id, a.kip_id 
--     FROM tbllichhoc a, tbllophocphan b, tbldangkihoc c, tblmonhockihoc d 
--     WHERE d.ki_hoc_id = 8 AND b.mon_hoc_ki_hoc_id = d.id 
--     AND a.lop_hoc_phan_id = b.id AND c.lop_hoc_phan_id = b.id 
--     AND c.sinh_vien_khoa_id = 5;

--     SELECT a.tuan_id, a.ngay_id, a.kip_id 
--     FROM tbllichhoc a, tbllophocphan b, tblmonhockihoc d 
--     WHERE d.ki_hoc_id = 8 AND b.mon_hoc_ki_hoc_id = d.id 
--     AND a.lop_hoc_phan_id = b.id AND b.id = 5;
    
-- 11- 16
-- 5-10
    
-- call SheduleOfSectionClass(11); 
-- tesst dang ki 5 va 11
-- call getSectionClass(5,3);
call inserttestv4(5, 5);
-- call getRegisterOfStudent(5,8); 


 
--         SELECT EXISTS (
--             SELECT 1
--             FROM (
--                 SELECT a.tuan_id, a.ngay_id, a.kip_id 
--                 FROM tbllichhoc a, tbllophocphan b, tbldangkihoc c, tblmonhockihoc d 
--                 WHERE d.ki_hoc_id = 8 AND b.mon_hoc_ki_hoc_id = d.id 
--                 AND a.lop_hoc_phan_id = b.id AND c.lop_hoc_phan_id = b.id 
--                 AND c.sinh_vien_khoa_id = 5
--             ) AS lich_hoc_1
--             JOIN (
--                 SELECT a.tuan_id, a.ngay_id, a.kip_id 
-- 				FROM tbllichhoc a, tbllophocphan b, tblmonhockihoc d 
-- 				WHERE d.ki_hoc_id = 8 AND b.mon_hoc_ki_hoc_id = d.id 
-- 				AND a.lop_hoc_phan_id = b.id AND b.id = 11
--             ) AS lich_hoc_2
--             USING (tuan_id, ngay_id, kip_id)
--         )
--     ;
