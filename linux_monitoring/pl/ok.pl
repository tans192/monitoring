#!/usr/bin/perl
use Net::Stomp;

$ip	= "192.168.1.56";

my $stomp = Net::Stomp->new( { hostname => '192.168.1.56', port => '61613' } );
$stomp->connect({login => 'aq', passcode => 'aqadmin'}); 
	my $message = "$ip\~ok";
	print "$message\n";
	$stomp -> send( { destination => '/queue/info', persistent => 'true',body => $message} );
	sleep 1;
$stomp ->disconnect;
exit;
