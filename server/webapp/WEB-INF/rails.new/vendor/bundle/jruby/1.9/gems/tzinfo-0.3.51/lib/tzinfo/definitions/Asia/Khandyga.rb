# encoding: UTF-8

module TZInfo
  module Definitions
    module Asia
      module Khandyga
        include TimezoneDefinition
        
        timezone 'Asia/Khandyga' do |tz|
          tz.offset :o0, 32533, 0, :LMT
          tz.offset :o1, 28800, 0, :YAKT
          tz.offset :o2, 32400, 0, :YAKT
          tz.offset :o3, 32400, 3600, :YAKST
          tz.offset :o4, 28800, 3600, :YAKST
          tz.offset :o5, 36000, 0, :VLAT
          tz.offset :o6, 36000, 3600, :VLAST
          tz.offset :o7, 39600, 0, :VLAT
          tz.offset :o8, 36000, 0, :YAKT
          
          tz.transition 1919, 12, :o1, 209287335467, 86400
          tz.transition 1930, 6, :o2, 14556889, 6
          tz.transition 1981, 3, :o3, 354898800
          tz.transition 1981, 9, :o2, 370706400
          tz.transition 1982, 3, :o3, 386434800
          tz.transition 1982, 9, :o2, 402242400
          tz.transition 1983, 3, :o3, 417970800
          tz.transition 1983, 9, :o2, 433778400
          tz.transition 1984, 3, :o3, 449593200
          tz.transition 1984, 9, :o2, 465325200
          tz.transition 1985, 3, :o3, 481050000
          tz.transition 1985, 9, :o2, 496774800
          tz.transition 1986, 3, :o3, 512499600
          tz.transition 1986, 9, :o2, 528224400
          tz.transition 1987, 3, :o3, 543949200
          tz.transition 1987, 9, :o2, 559674000
          tz.transition 1988, 3, :o3, 575398800
          tz.transition 1988, 9, :o2, 591123600
          tz.transition 1989, 3, :o3, 606848400
          tz.transition 1989, 9, :o2, 622573200
          tz.transition 1990, 3, :o3, 638298000
          tz.transition 1990, 9, :o2, 654627600
          tz.transition 1991, 3, :o4, 670352400
          tz.transition 1991, 9, :o1, 686080800
          tz.transition 1992, 1, :o2, 695757600
          tz.transition 1992, 3, :o3, 701802000
          tz.transition 1992, 9, :o2, 717526800
          tz.transition 1993, 3, :o3, 733251600
          tz.transition 1993, 9, :o2, 748976400
          tz.transition 1994, 3, :o3, 764701200
          tz.transition 1994, 9, :o2, 780426000
          tz.transition 1995, 3, :o3, 796150800
          tz.transition 1995, 9, :o2, 811875600
          tz.transition 1996, 3, :o3, 828205200
          tz.transition 1996, 10, :o2, 846349200
          tz.transition 1997, 3, :o3, 859654800
          tz.transition 1997, 10, :o2, 877798800
          tz.transition 1998, 3, :o3, 891104400
          tz.transition 1998, 10, :o2, 909248400
          tz.transition 1999, 3, :o3, 922554000
          tz.transition 1999, 10, :o2, 941302800
          tz.transition 2000, 3, :o3, 954003600
          tz.transition 2000, 10, :o2, 972752400
          tz.transition 2001, 3, :o3, 985453200
          tz.transition 2001, 10, :o2, 1004202000
          tz.transition 2002, 3, :o3, 1017507600
          tz.transition 2002, 10, :o2, 1035651600
          tz.transition 2003, 3, :o3, 1048957200
          tz.transition 2003, 10, :o2, 1067101200
          tz.transition 2003, 12, :o5, 1072882800
          tz.transition 2004, 3, :o6, 1080403200
          tz.transition 2004, 10, :o5, 1099152000
          tz.transition 2005, 3, :o6, 1111852800
          tz.transition 2005, 10, :o5, 1130601600
          tz.transition 2006, 3, :o6, 1143302400
          tz.transition 2006, 10, :o5, 1162051200
          tz.transition 2007, 3, :o6, 1174752000
          tz.transition 2007, 10, :o5, 1193500800
          tz.transition 2008, 3, :o6, 1206806400
          tz.transition 2008, 10, :o5, 1224950400
          tz.transition 2009, 3, :o6, 1238256000
          tz.transition 2009, 10, :o5, 1256400000
          tz.transition 2010, 3, :o6, 1269705600
          tz.transition 2010, 10, :o5, 1288454400
          tz.transition 2011, 3, :o7, 1301155200
          tz.transition 2011, 9, :o8, 1315832400
          tz.transition 2014, 10, :o2, 1414252800
        end
      end
    end
  end
end
