use strict ;

use blib ;


use Inline Java => <<'END_OF_JAVA_CODE' ;

class Pod_alu {
      public Pod_alu(){
      }

      public int add(int i, int j){
         return i + j ;
      }

      public int subtract(int i, int j){
         return i - j ;
      }
   }   
END_OF_JAVA_CODE


my $alu = new Pod_alu() ;
print($alu->add(9, 16) . "\n") ; # prints 25
print($alu->subtract(9, 16) . "\n") ; # prints -7


