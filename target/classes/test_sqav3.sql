use register;

DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `inserttestv2`(IN idSVK INT, IN idLHP INT)
BEGIN
    DECLARE lhp_exists BOOLEAN;
    DECLARE monhoc_id INT;  
    DECLARE lhp INT;
    
    SELECT EXISTS(SELECT 1 FROM tbldangkihoc WHERE lop_hoc_phan_id = idLHP) INTO lhp_exists;
    
    -- Selecting monhoc_id with limit 1 to avoid multiple rows error
    SELECT mh.id INTO monhoc_id
    FROM tblmonhoc as mh 
    LEFT JOIN tblmonhockihoc as mhkh ON mh.id = mhkh.mon_hoc_id
    LEFT JOIN tbllophocphan as lhp ON lhp.mon_hoc_ki_hoc_id = mhkh.id
    WHERE idLHP = lhp.id
    LIMIT 1;
    
    SELECT dkh.lop_hoc_phan_id INTO lhp
    FROM tbldangkihoc as dkh
    LEFT JOIN tbllophocphan as lhp ON lhp.id = dkh.lop_hoc_phan_id
    LEFT JOIN tblmonhockihoc as mhkh ON mhkh.id = lhp.mon_hoc_ki_hoc_id
    LEFT JOIN tblmonhoc as mh ON mh.id = mhkh.mon_hoc_id
    WHERE mh.id = monhoc_id
    LIMIT 1;
    
    IF lhp_exists THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Existed section class';
    ELSE
        IF lhp > 0 THEN
            CALL DeleteOneRegistration(idSVK, lhp);
            CALL InsertRegistration(idSVK, idLHP);
        ELSE
            INSERT INTO tbldangkihoc(sinh_vien_khoa_id, lop_hoc_phan_id) 
            VALUES(idSVK, idLHP);
        END IF;
    END IF;
END$$

DELIMITER ;