use strict ;
use Test ;

use Inline Config => 
           DIRECTORY => './_Inline_test';

use Inline(
	Java => 'DATA'
) ;


BEGIN {
	plan(tests => 10) ;
}


# Methods
ok(types6->get("key"), undef) ;
my $t = new types6("key", "value") ;

{
	ok($t->get("key"), "value") ;
	
	# Members
	ok($types6::i == 5) ;
	$types6::i = 7 ;
	ok($t->{i} == 7) ;
	
	my $t2 = new types6("key2", "value2") ;
	my $hm = $types6::hm ;
	$types6::hm = undef ;
	ok(types6->get($hm, "key2"), "value2") ;
	
	$types6::hm = $hm ;
	ok($t2->get("key2"), "value2") ;
	
	# Calling an instance method without an object reference
	eval {types6->set()} ; ok($@, qr/must be called from an object reference/) ;

	# Put in back like before...
	$types6::i = 5 ;
	ok($types6::i == 5) ;
	my $tt = new types6("key", undef) ;
	ok($tt->get("key"), undef) ;
}

# Since $types::hm was returned to the Perl space, it was registered in the object 
# HashMap.
ok($t->__get_private()->{proto}->ObjectCount(), 2) ;


__END__

__Java__


import java.util.* ;


class types6 {
	public static int i = 5 ;
	public static HashMap hm = new HashMap() ;

	public types6(String k, String v){
		hm.put(k, v) ;
	}

	public static String get(String k){
		return (String)hm.get(k) ; 
	}

	public static String get(HashMap h, String k){
		return (String)h.get(k) ; 
	}

	public String set(){
		return "set" ;
	}
}

