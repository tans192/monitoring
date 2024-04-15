#!/usr/bin/perl
use Net::Stomp;

@array = qw(cpu mem disk tomcat8080);

$ip					= "192.168.1.56";
$cpu        		= "cpu";                    
$mem        		= "mem";
$disk       		= "disk";
$io     			= "io";

$tomcat8080			= "tomcat8080";


my $stomp = Net::Stomp->new( { hostname => '192.168.1.56', port => '61613' } );
$stomp->connect({login => 'aq', passcode => 'aqadmin'}); 
for($index=0;$index<@array; $index++){

	CPU: if($cpu eq  $array[$index] ){
		print "\n$array[$index]\n";
		my $IDLE =`vmstat 1 11 |sed 1,3d | awk 'BEGIN{sum=0} { sum=sum+\$15} END{ print sum/10}' `;
		$IDLE = int  (100 - $IDLE);
		my $message = "$ip\~$cpu\~$IDLE";
		print "$message\n";
		$stomp -> send( { destination => '/queue/info', persistent => 'true',body => $message} );
		sleep 1;
	}

	Memory: if($mem eq $array[$index]  ){
		print "\n$array[$index]\n";
		my $total =`free | grep 'Mem' | awk '{print \$2'}`;
		my $buffer =`free | grep '-' | awk '{print \$3'}`;
		my $IDLEMEM = int $buffer*100./$total;
		my $message = "$ip\~$mem\~$IDLEMEM";
		print "$message\n";
		$stomp -> send( { destination => '/queue/info', persistent => 'true',body => $message} );
		sleep 1;
	}

	Disk: if($disk eq  $array[$index] ){
		print "\n$array[$index]\n";
		my $stats = `df -v`;
		my @stat = split("\n", $stats);
		foreach my $item (@stat[1..$#stat]) {
			if(split(" ",$item) >= 5) {
				my @items = split(" ",$item);
				$diskused{$items[-1]} = $items[-2];
				my $dskmsg = "$items[-1]\~$items[-2]";
				chop $dskmsg;
				my $message = "$ip\~$disk\~$dskmsg";
				chomp $message;
				print $message,"\n";
				$stomp -> send( { destination => '/queue/info', persistent => 'true',body => $message} );
			}
			sleep 1;
		}
	}

	tomcat8080: if($tomcat8080 eq $array[$index] ){
		print "\n$array[$index]\n";
		my $Tom8080 =`netstat -an|awk  '{print\$1, \$4}'|grep '[^0-9]9090\$'|wc -l `;
		$Tom8080 = $Tom8080==0?0:1;
		my $message = "$ip\~$tomcat8080\~$Tom8080";
		chomp $message;
		print "$message\n";
		$stomp -> send( { destination => '/queue/info', persistent => 'true',body => $message} );			
		sleep 1;
	}
}
$stomp ->disconnect;
exit;
