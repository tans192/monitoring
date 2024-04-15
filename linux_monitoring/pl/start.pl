#!/usr/bin/perl

@array = qw(tomcat80 activemq appstart);

$ip					= "192.168.1.56";
$cpu        		= "cpu";                    
$mem        		= "mem";
$disk       		= "disk";
$io     			= "io";

$tomcat8080 		= "tomcat80";
$activemq			= "activemq";
$appstart			= "appstart";
$path               = "";

for($index=0;$index<@array; $index++){
	
	tomcat8080: if($tomcat8080 eq $array[$index] ){
		print "\n$array[$index]\n";
		$path = "/usr/soft/tomcat7/bin";
		chdir $path;
		print "$path\n";		
		my $Tom8080 =`netstat -an|awk  '{print\$1, \$4}'|grep '[^0-9]9090\$'|wc -l `;
		$Tom8080 = $Tom8080==0?0:1;
		print "$Tom8080\n";
		if($Tom8080 eq ("0")){
			system("./startup.sh");
		}
		sleep 1;
	}
	
	activemq: if($activemq eq $array[$index] ){
		print "\n$array[$index]\n";
		$path = "/usr/soft/activemq/bin";
		chdir $path;
		print "$path\n";
		my $Tom80 =`ps -ef|grep /usr/soft/activemq/bin/run.jar |grep -v grep|wc -l`; 
		$Tom80 = $Tom80==0?0:1;
		print "$Tom80\n";
		if($Tom80 eq ("0")){
			system("./activemq start");
		}
		sleep 1;
	}
	
	appstart: if($appstart eq $array[$index] ){
		print "\n$array[$index]\n";
		$path = "/usr/soft/activemqmonitoring/monitorApp";
		chdir $path;
		print "$path\n";
		my $Tom80 =`ps -ef|grep com.hyaroma.blog.jiankong.AppListener |grep -v grep|wc -l`; 
		$Tom80 = $Tom80==0?0:1;
		print "$Tom80\n";
		if($Tom80 eq ("0")){
			system("./run.sh");
		}
		sleep 1;
	}	
}
exit;
