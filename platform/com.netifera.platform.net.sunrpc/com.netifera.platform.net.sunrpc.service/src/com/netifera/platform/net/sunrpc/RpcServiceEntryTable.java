package com.netifera.platform.net.sunrpc;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

// TODO parse from text file in this bundle?
// TODO known invalid version/program

public final class RpcServiceEntryTable {
	private static final Map<String, Integer> numberMap = new HashMap<String, Integer>();
	private static final Map<Integer, String> nameMap = new TreeMap<Integer, String>();
	private static final Map<String, String> aliasesMap = new HashMap<String, String>();
	private static final Map<Integer, int[]> invalidVersions = new HashMap<Integer, int[]>();
	
	private RpcServiceEntryTable() {} // singleton
	
	// generated using com.netifera.platform.net.sunrpc.service.EtcRpcParser
	static {
		register(100000, "rpcbind", new String[] {"portmap", "sunrpc"});
		register(100001, "rstatd", new String[] {"perfmeter", "rstat", "rstat_svc", "rup"});
		register(100002, "rusersd", new String[] {"rusers"});
		register(100003, "nfs", new String[] {"nfsd", "nfsprog"});
		register(100004, "ypserv", new String[] {"ypprog"});
		register(100005, "mountd", new String[] {"mount", "showmount"});
		register(100006, "remote_dbx");
		register(100007, "ypbind");
		register(100008, "walld", new String[] {"rwall", "shutdown"});
		register(100009, "yppasswdd", new String[] {"yppasswd"});
		register(100010, "etherstatd", new String[] {"etherstat"});
		register(100011, "rquotad", new String[] {"quota", "rquota", "rquotaprog"});
		register(100012, "sprayd", new String[] {"spray"});
		register(100013, "3270_mapper");
		register(100014, "rje_mapper"); // Remote job entry mapping service.
		register(100015, "selection_svc", new String[] {"selnsvc"});
		register(100016, "database_svc", new String[] {"dbms", "dbsessionmgr", "netdbms", "unify"});
		register(100017, "rexd", new String[] {"remote_exec", "rex"});
		register(100018, "alis", new String[] {"alice", "office_auto"});
		register(100019, "sched");
		register(100020, "llockmgr");
		register(100021, "nlockmgr");
		register(100022, "x25.inr");
		register(100023, "statmon");
		register(100024, "status", new String[] {"rpc.statd", "statd"}); // Status monitor
		register(100025, "select_lib");
		register(100026, "bootparam");
		register(100027, "mazewars"); // Mazewars game
		register(100028, "ypupdated", new String[] {"ypupdate"});
		register(100029, "keyserv", new String[] {"keyserver"});
		register(100030, "securelogin");
		register(100031, "nfs_fwdlnit"); // NFS network forwarding service.
		register(100032, "nfs_fwdtrns"); // NFS forwarding transmitter
		register(100033, "sunlink_mapper");
		register(100034, "net_monitor");
		register(100035, "database"); // Lightweight database
		register(100036, "passwd_auth");
		register(100037, "tfsd"); // Translucent file service.
		register(100038, "nsed");
		register(100039, "nsemntd");
		register(100040, "pfs_mountd");
		register(100043, "showfhd", new String[] {"showfh"});
		register(100044, "mvsmount"); // MVSmount daemon (for mvslogin mvslogout)
		register(100055, "ioadmd", new String[] {"rpc.ioadmd"});
		register(100059, "showattrd"); // DFSMS/MVS NFS server
		register(100062, "NETlicense");
		register(100065, "sunisamd");
		register(100066, "debug_svc", new String[] {"dbsrv"});
		register(100068, "cmsd", new String[] {"dtcalendar", "rpc.cmsd"});
		register(100069, "ypxfrd", new String[] {"rpc.ypxfrd", "ypxfr"});
		register(100071, "bugtraqd");
		register(100078, "kerbd");
		register(100080, "rpc.operd", new String[] {"opermsg"}); // Sun Online-Backup
		register(100083, "ttdbserverd", new String[] {"rpc.ttdbserverd"});
		register(100087, "admind");
		register(100099, "autofsd");
		register(100101, "event", new String[] {"na.event"}); // SunNet Manager
		register(100102, "logger", new String[] {"na.logger"}); // SunNet Manager
		register(100104, "sync", new String[] {"na.sync"});
		register(100105, "diskinfo", new String[] {"na.diskinfo"}); // SunNet Manager
		register(100106, "iostat", new String[] {"na.iostat"});
		register(100107, "hostperf", new String[] {"na.hostperf"});
		register(100109, "activity", new String[] {"na.activity"}); // SunNet Manager
		register(100110, "db_mgr");
		register(100111, "lpstat", new String[] {"na.lpstat"}); // SunNet Manager
		register(100112, "hostmem", new String[] {"na.hostmem"});
		register(100113, "sample", new String[] {"na.sample"});
		register(100114, "x25", new String[] {"na.x25"});
		register(100115, "ping", new String[] {"na.ping"});
		register(100116, "rpcnfs", new String[] {"na.rpcnfs"});
		register(100117, "hostif", new String[] {"na.hostif"});
		register(100118, "etherif", new String[] {"na.etherif"});
		register(100119, "ippath", new String[] {"na.ippath"}); // SunNet Manager
		register(100120, "iproutes", new String[] {"na.iproutes"});
		register(100121, "layers", new String[] {"na.layers"});
		register(100122, "snmp", new String[] {"na.snmp", "snmp-cmc", "snmp-synoptics", "snmp-unisys", "snmp-utk"});
		register(100123, "traffic", new String[] {"na.traffic"});
		register(100124, "DNInode", new String[] {"DNIneT", "na.dni"});
		register(100131, "layers2", new String[] {"na.layers2"}); // SunNet Manager
		register(100133, "nsm_addrand"); // Solaris's statd NSM
		register(100134, "ktkt_warnd"); // Kerberos warning daemon
		register(100135, "etherif2", new String[] {"na.etherif2"}); // SunNet Manager
		register(100136, "hostmem2", new String[] {"na.hostmem2"}); // SunNet Manager
		register(100137, "iostat2", new String[] {"na.iostat2"}); // SunNet Manager
		register(100138, "snmpv2", new String[] {"na.snmpv2"}); // SNM Version 2.2.2
		register(100139, "sender", new String[] {"cc_sender"}); // Cooperative Consoles
		register(100146, "amiserv"); // AMI Daemon
		register(100147, "amiaux"); // AMI Daemon
		register(100150, "ocfserv"); // OCF (Smart card) Daemon
		register(100153, "sunvts");
		register(100155, "smserverd", new String[] {"rpc.smserverd"}); // support removable media devices
		register(100221, "kcms_server"); // SunKCMS Profile Server
		register(100227, "nfs_acl");
		register(100229, "metad");
		register(100230, "metamhd");
		register(100231, "nfsauth");
		register(100232, "sadmind"); // Solstice
		register(100233, "ufsd");
		register(100234, "gssd", new String[] {"gss"}); // GSS Daemon
		register(100235, "cachefsd", new String[] {"cachefs"}); // CacheFS Daemon
		register(100242, "rpc.metamedd"); // SUNWmdm - Sun Cluster
		register(100244, "sm_symond"); // Solstice SyMON process controller
		register(100248, "rpc.pmfd", new String[] {"pmfd"}); // Sun Cluster - process monitor server
		register(100249, "snmpXdmid");
		register(100281, "rpc.metacld"); // SUNWmdm - Sun Cluster
		register(100300, "nisd", new String[] {"rpc.nisd"});
		register(100301, "nis_cache");
		register(100302, "nis_callback");
		register(100303, "nispasswd", new String[] {"rpc.nispasswdd"});
		register(100304, "fnsypd"); // Federated Naming Service (FNS)
		register(100422, "mdcommd"); // SVM Multi Node Communication Daemon
		register(100424, "stfsloader"); // Standard Type Services Framework (STSF) Font Server
		register(105004, "rpc.pts", new String[] {"Protoserver"}); // Advanced Printing Software
		register(120100, "swu_svr"); // Software Usage Monitoring daemon
		register(120126, "nf_snmd"); // SunNet Manager
		register(120127, "nf_snmd");
		register(150001, "pcnfsd", new String[] {"pcnfs"});
		register(200000, "PyramidLock");
		register(200001, "PyramidSys5"); // Sys5
		register(200002, "CADDS_Image"); // CV CADDS images.
		register(200005, "pdbDA");
		register(200020, "SWG", new String[] {"swg"}); // DMFE/DAWS (Defense Automated Warning System)
		register(200023, "exportmap");
		register(200034, "Gqsrv", new String[] {"gqsrv"});
		register(200035, "Ppt", new String[] {"ppt"});
		register(200036, "Pmt", new String[] {"pmt"});
		register(200037, "Msgt", new String[] {"msgt"});
		register(200038, "Walerts", new String[] {"walerts"});
		register(200039, "Mgt", new String[] {"mgt"});
		register(200040, "Pft", new String[] {"pft"});
		register(200041, "Msgq", new String[] {"msgq"});
		register(200042, "Smpsrv", new String[] {"smpsrv"});
		register(200043, "Dexsrv", new String[] {"dexsrv"});
		register(200044, "Statussrv", new String[] {"statussrv"});
		register(200046, "SessionServer", new String[] {"sessionserver"});
		register(200047, "SessionDaemon", new String[] {"sessiondaemon"});
		register(200048, "Pmsgq", new String[] {"pmsgq"});
		register(200049, "Filesrv", new String[] {"filesrv"});
		register(200050, "Magfetch", new String[] {"magfetch"});
		register(200051, "Optfetch", new String[] {"optfetch"});
		register(200052, "Securitysrv", new String[] {"securitysrv"});
		register(200100, "bundle"); // Delay Tolerant Networking - DTN agent
		register(200200, "bundle_demux"); // Delay Tolerant Networking - DTN agent
		register(200201, "ecodisc");
		register(200202, "ecolic");
		register(200203, "eamon");
		register(200205, "ecoad");
		register(211637, "rpc.dbserv", new String[] {"dbserv", "rpc.dbserv_dir"});
		register(217843, "rpc.taped", new String[] {"rpc.taped_dir", "taped"});
		register(217854, "rpc.taped", new String[] {"rpc.taped_dir", "taped"});
		register(300001, "ADTFileLock"); // ADT file locking service.
		register(300004, "rpc.frameusersd"); // FrameMaker
		register(300006, "fmclient"); // FrameMaker Client
		register(300007, "fmeditor"); // FrameMaker Editor
		register(300009, "fmserver", new String[] {"FrameServer", "stdfm"}); // FrameMaker Server
		register(300019, "amd", new String[] {"amq"});
		register(300021, "Steering"); // Steering Library
		register(300029, "rpc.ldmd", new String[] {"ldm"}); // Unidata LDM
		register(300030, "UpdtAuditsS");
		register(300091, "Dbpass", new String[] {"dbpass"});
		register(300145, "clms"); // CenterLine CodeCenter
		register(300214, "fm_flb"); // FrameMaker
		register(300215, "fm_fls"); // FrameMaker licnese server
		register(300301, "acuserve");
		register(300326, "dr_daemon"); // Sun Enterprise Server Alternate Pathing
		register(300375, "rmd");
		register(300376, "agcd");
		register(300433, "bssd", new String[] {"bss"});
		register(300434, "drdd", new String[] {"drd"});
		register(300473, "ap_daemon"); // SUNWapu - Alternate Pathing (AP)
		register(300483, "cnxmond"); // cluster node monitor (Digital UNIX)
		register(300484, "cnxagentd"); // cluster agent (Digital UNIX)
		register(300516, "mcserv");
		register(300527, "cluinfod"); // cluster information server (Digital UNIX)
		register(300598, "dmispd"); // Sun Solstice Enterprise DMI Service Provider
		register(300632, "prpasswd");
		register(300664, "ks"); // ACPLT/KS protocol
		register(344444, "sfs"); // SFS - Self-Certifying File System
		register(351455, "mapsvc");
		register(351457, "berkeleydb"); // Sleepycat Software: Berkeley DB
		register(351473, "caasalert");
		register(390100, "prestoctl_svc", new String[] {"presto"}); // Prestoserve control daemon
		register(390101, "rap", new String[] {"rapla"});
		register(390102, "rapserv", new String[] {"raprd"});
		register(390103, "nsrd", new String[] {"nsr"}); // NetWorker service
		register(390104, "nsrmmd", new String[] {"nsrmm"}); // NetWorker media mupltiplexor daemon
		register(390105, "nsrindexd", new String[] {"nsrindex"}); // NetWorker file index daemon
		register(390107, "nsrmmdbd", new String[] {"nsrmmdb"}); // NetWorker media management database daemon
		register(390109, "nsrstat");
		register(390110, "nsrjb", new String[] {"nsrjbd"}); // NetWorker jukebox-control service
		register(390113, "nsrexec", new String[] {"nsrexecd"}); // NetWorker client execution service
		register(390115, "lgtolmd"); // Legato license daemon
		register(390400, "nsrnotd"); // NetWorker notary service
		register(390600, "arserverd");
		register(390601, "ntserverd");
		register(390602, "ntclientd");
		register(390603, "aresclsrv");
		register(390604, "arservtcd");
		register(390610, "flashservd");
		register(390619, "arflashbd");
		register(390620, "arfastsrv");
		register(390621, "arfastsrv");
		register(390622, "arfastsrv");
		register(390623, "arfastsrv");
		register(390624, "arfastsrv");
		register(390625, "arfastsrv");
		register(390626, "arfastsrv");
		register(390627, "arfastsrv");
		register(390628, "arfastsrv");
		register(390629, "arfastsrv");
		register(390630, "arfastsrv");
		register(390631, "arfastsrv");
		register(390632, "arfastsrv");
		register(390633, "arfastsrv");
		register(390634, "arfastsrv");
		register(390635, "arlistsrv");
		register(390636, "arlistsrv");
		register(390637, "arlistsrv");
		register(390638, "arlistsrv");
		register(390639, "arlistsrv");
		register(390640, "arlistsrv");
		register(390641, "arlistsrv");
		register(390642, "arlistsrv");
		register(390643, "arlistsrv");
		register(390644, "arlistsrv");
		register(390645, "arlistsrv");
		register(390646, "arlistsrv");
		register(390647, "arlistsrv");
		register(390648, "arlistsrv");
		register(390649, "arlistsrv");
		register(391000, "sgi_snoopd", new String[] {"snoop", "snoopd"});
		register(391001, "sgi_toolkitbus");
		register(391002, "sgi_fam", new String[] {"fam"});
		register(391003, "sgi_notepad", new String[] {"notepad"});
		register(391004, "sgi_mountd", new String[] {"mount", "showmount"});
		register(391005, "sgi_smtd", new String[] {"smtd"});
		register(391006, "sgi_pcsd", new String[] {"pcsd"});
		register(391007, "sgi_nfs");
		register(391008, "sgi_rfind", new String[] {"rfind"});
		register(391009, "sgi_pod", new String[] {"pod"});
		register(391010, "sgi_iphone");
		register(391011, "sgi_videod");
		register(391012, "sgi_testcd", new String[] {"testcd"});
		register(391013, "sgi.ha_hbeat", new String[] {"ha_hbeat", "ha_heartbeat", "sgi_ha_hb"});
		register(391014, "sgi.ha_nc", new String[] {"ha_nc"});
		register(391015, "sgi.ha_appmon", new String[] {"ha_appmon"});
		register(391016, "sgi_xfsmd");
		register(391017, "sgi_mediad", new String[] {"mediad"});
		register(391018, "sgi.ha_orcl", new String[] {"ha_orcl"});
		register(391019, "sgi.ha_ifmx", new String[] {"ha_ifmx"});
		register(391020, "sgi.ha_sybs", new String[] {"ha_sybs"});
		register(391021, "sgi.ha_ifa", new String[] {"ha_ifa"});
		register(391022, "sgi_reserved");
		register(391023, "sgi_reserved");
		register(391024, "sgi_reserved");
		register(391025, "sgi_reserved");
		register(391026, "sgi_reserved");
		register(391027, "sgi_reserved");
		register(391028, "sgi_reserved");
		register(391029, "sgi_espd", new String[] {"espd"});
		register(391030, "sgi_reserved");
		register(391031, "sgi_reserved");
		register(391032, "sgi_reserved");
		register(391033, "sgi_reserved");
		register(391034, "sgi_reserved");
		register(391035, "sgi_reserved");
		register(391036, "sgi_reserved");
		register(391037, "sgi_reserved");
		register(391038, "sgi_reserved");
		register(391039, "sgi_reserved");
		register(391040, "sgi_reserved");
		register(391041, "sgi_reserved");
		register(391042, "sgi_reserved");
		register(391043, "sgi_reserved");
		register(391044, "sgi_reserved");
		register(391045, "sgi_reserved");
		register(391046, "sgi_reserved");
		register(391047, "sgi_reserved");
		register(391048, "sgi_reserved");
		register(391049, "sgi_reserved");
		register(391050, "sgi_reserved");
		register(391051, "sgi_reserved");
		register(391052, "sgi_reserved");
		register(391053, "sgi_reserved");
		register(391054, "sgi_reserved");
		register(391055, "sgi_reserved");
		register(391056, "sgi_reserved");
		register(391057, "sgi_reserved");
		register(391058, "sgi_reserved");
		register(391059, "sgi_reserved");
		register(391060, "sgi_reserved");
		register(391061, "sgi_reserved");
		register(391062, "sgi_reserved");
		register(391063, "sgi_reserved");
		register(391200, "afsd");
		register(391201, "dhc");
		register(391202, "cmsd", new String[] {"dtcalendar", "rpc.cmsd"});
		register(391203, "xxx2trif");
		register(391204, "trif2xxx");
		register(391205, "dx");
		register(391206, "licd");
		register(391434, "ingsqld"); // SCO JDBC
		register(395175, "asedirector");
		register(395176, "aseagent");
		register(395177, "asehsm");
		register(395179, "aselogger");
		register(395250, "pnictl");
		register(395644, "caservd", new String[] {"as6_arcserve"});
		register(395645, "calqserver", new String[] {"LQServer", "as6_queue"});
		register(395646, "camediadsvr", new String[] {"MediaSvr", "as6_tapesvr"});
		register(395647, "caldbserver", new String[] {"LDbserver", "as6_dbserver"});
		register(395648, "caauthd", new String[] {"as6_auth"});
		register(395649, "cadiscovd", new String[] {"as6_discovery"});
		register(395650, "caloggerd", new String[] {"as6_logger"});
		register(450000, "EnsignAgent"); // Ensign Agent
		register(900101, "drac"); // Dynamic Relay Authorization Control
		register(1000002, "AdoIfServer"); // RHIC AdoIf Server (Accelerator Device Object)
		register(2000004, "notifServer"); // RHIC notifServer
		register(20000000, "retherif", new String[] {"na.retherif"});
		register(20000001, "genagnt", new String[] {"na.genagnt"});
		register(20000114, "acedb", new String[] {"rpc.acedbd"});
		register(20000115, "aboutdb", new String[] {"rpc.acedbd"});
		register(20000116, "aatdb", new String[] {"rpc.acedbd"});
		register(20000777, "seagent"); // Memco/Platinum/CA SeOS security product
		register(21000023, "dbsrvr"); // TACO
		register(28000205, "egts"); // Eurogam Tape Server (10205/udp)
		register(28000220, "ers"); // Eurogam Register Server (10220/udp)
		register(28000230, "sas"); // Spectrum Access Server (10230/udp)
		register(200100001, "netinfobind", new String[] {"nibindd"}); // NeXT NetInfo
		register(200100002, "renderd"); // NeXT renderd
		register(536870913, "SLSd_daemon"); // HP Distributed Single Logical Screen
		register(536870915, "uidd");
		register(536870916, "vtsk"); // SunVTS diagnostic kernel
		register(536870917, "bondd");
		register(536870918, "staticd");
		register(536870919, "curved");
		register(536870920, "msglogd");
		register(536870921, "aliasd");
		register(536870922, "ticketd");
		register(536870923, "glossd");
		register(536870924, "futured");
		register(536870928, "priced");
		register(536870929, "ladderd");
		register(536870937, "optiond"); // FIRM option server
		register(536870944, "ndbportmgr");
		register(536870945, "ndbserver1");
		register(536870946, "ndbserver2");
		register(536870947, "ndbserver3");
		register(536870948, "ndbserver4");
		register(536870949, "ndbserver5");
		register(536870950, "ndbserver6");
		register(536870951, "ndbserver7");
		register(536870952, "ndbserver8");
		register(536870953, "ndbserver9");
		register(536870954, "ndbserver10");
		register(536870955, "ndbserver11");
		register(536870956, "ndbserver12");
		register(536870957, "ndbserver13");
		register(536870958, "ndbserver14");
		register(536870959, "ndbserver15");
		register(536870960, "ndbserver16");
		register(536870961, "ndbserver17");
		register(536870962, "ndbserver18");
		register(536870963, "ndbserver19");
		register(536870964, "ndbserver20");
		register(536870965, "ndbserver21");
		register(536870966, "ndbserver22");
		register(536870967, "ndbserver23");
		register(536870968, "ndbserver24");
		register(536870969, "ndbserver25");
		register(536870970, "ndbserver26");
		register(536870971, "ndbserver27");
		register(536870972, "ndbserver28");
		register(536870973, "ndbserver29");
		register(536870974, "ndbserver30");
		register(536870975, "ndbserver31");
		register(536870976, "ndbserver32");
		register(536870977, "ndbserver33");
		register(536870978, "ndbserver34");
		register(536870979, "ndbserver35");
		register(536870980, "ndbserver36");
		register(536870981, "ndbserver37");
		register(536870982, "ndbserver38");
		register(536870983, "ndbserver39");
		register(536870984, "ndbserver40");
		register(536870985, "ndbserver41");
		register(536870986, "ndbserver42");
		register(536870987, "ndbserver43");
		register(536870988, "ndbserver44");
		register(536870989, "ndbserver45");
		register(536870990, "ndbserver46");
		register(536870991, "ndbserver47");
		register(536870992, "ndbserver48");
		register(536870993, "ndbserver49");
		register(536870994, "ndbserver50");
		register(536870995, "ndbserver51");
		register(536870996, "ndbserver52");
		register(536870997, "ndbserver53");
		register(536870998, "ndbserver54");
		register(536870999, "ndbserver55");
		register(536871000, "ndbserver56");
		register(536871001, "ndbserver57");
		register(536871002, "ndbserver58");
		register(536871003, "ndbserver59");
		register(536871004, "ndbserver60");
		register(536871005, "ndbserver61");
		register(536871006, "ndbserver62");
		register(536871007, "ndbserver63");
		register(536871008, "ndbserver64");
		register(536871009, "ndbserver65");
		register(536871010, "ndbserver66");
		register(536871011, "ndbserver67");
		register(536871012, "ndbserver68");
		register(536871013, "ndbserver69");
		register(536871014, "ndbserver70");
		register(536871015, "ndbserver71");
		register(536871016, "ndbserver72");
		register(536871017, "ndbserver73");
		register(536871018, "ndbserver74");
		register(536871019, "ndbserver75");
		register(536871020, "ndbserver76");
		register(536871021, "ndbserver77");
		register(536871022, "ndbserver78");
		register(536871023, "ndbserver79");
		register(536871024, "ndbserver80");
		register(536871025, "ndbserver81");
		register(536871026, "ndbserver82");
		register(536871027, "ndbserver83");
		register(536871028, "ndbserver84");
		register(536871029, "ndbserver85");
		register(536871030, "ndbserver86");
		register(536871031, "ndbserver87");
		register(536871032, "ndbserver88");
		register(536871033, "ndbserver89");
		register(536871034, "ndbserver90");
		register(536871035, "ndbserver91");
		register(536871036, "ndbserver92");
		register(536871037, "ndbserver93");
		register(536871038, "ndbserver94");
		register(536871039, "ndbserver95");
		register(536871040, "ndbserver96");
		register(536871041, "ndbserver97");
		register(536871042, "ndbserver98");
		register(536871043, "ndbserver99");
		register(536871044, "ndbserver100");
		register(536871680, "gnbk"); // ACEDB genome database package
		register(537208899, "katie_mount");
		register(537208900, "katie_nfs", new String[] {"katie"});
		register(541414217, "fcagent"); // SGI FibreVault Status/Configuration daemon
		register(545580417, "ugidd", new String[] {"rpc.ugidd"});
		register(555555554, "inetray.start");
		register(555555555, "inetray");
		register(555555556, "inetray");
		register(555555557, "inetray");
		register(555555558, "inetray");
		register(555555559, "inetray");
		register(555555560, "inetray");
		register(591751041, "pnmd"); // SunCluster - Public Network Management (PNM)
		register(600000001, "collectd", new String[] {"collect"}); // IRE Computer
		register(600000002, "xycomd", new String[] {"xycom"}); // IRE Computer
		register(600000003, "motord", new String[] {"motor"}); // IRE Computer
		register(600000004, "fitsd", new String[] {"fits", "writer"}); // Control Room computer
		register(600100029, "des_crypt", new String[] {"freebsd-crypt"}); // FreeBSD
		register(600100069, "fypxfrd", new String[] {"freebsd-ypxfrd"}); // FreeBSD
		register(611319808, "rdbx");
		register(630474513, "bminrd"); // MacroModel - BatchMin Network Server
		register(788585389, "bwnfsd"); // (PC)NFS server by Beame & Whiteside, Inc.
		register(805306368, "dmispd"); // Sun Solstice Enterprise DMI Service Provider
		register(805310465, "sql_disp"); // GNU SQL Server
		register(805898569, "rdict"); // "Internetworking with TCP/IP Vol 3"
		register(806422610, "piktc_svc"); // PIKT: Problem Informant/Killer Tool
		register(822087681, "testpoint"); // Test point server
		register(822087682, "awg"); // Arbitrary waveform generator
		register(822087683, "cgdsrtdd"); // Real-time data server
		register(822087684, "gdsd"); // Diagnostics message server
		register(822087685, "chnconfd"); // Channel database daemon for gds
		register(822087686, "leapconfd"); // Leap second information daemon
		register(822087687, "rlaunchd"); // Remote program launcher
		register(824395111, "cfsd");
		register(912680550, "cns"); // Controls Name Server
		register(1073741824, "fmproduct", new String[] {"_Frame_RPC"}); // FrameMaker
		register(1073741840, "gsql_trn"); // GNU SQL Server
		register(1092830567, "cfsd");
		register(1145324612, "rdb"); // Wind River Systems' VxWorks debug stub
		register(1289637086, "dtcm");
		register(1289637087, "ttsession"); // Sun Solaris 7 (2.7) and up.
		register(1342177279, "ttsession"); // Sun Solaris upto 2.6
		
		invalidVersions.put(100003, new int[] {0, 3});
		invalidVersions.put(100021, new int[] {0, 2});
		invalidVersions.put(100227, new int[] {0});
	}
	
	public static boolean isValidVersion(int program, int version) {
		if (!invalidVersions.containsKey(program)) {
			return true;
		}
		for (int invalidVersion : invalidVersions.get(program)) {
			if (invalidVersion == version) {
				return false;
			}
		}
		return true;
	}
	
	public static Integer[] registeredNumbers() {
		Integer[] array = new Integer[nameMap.keySet().size()];
		return nameMap.keySet().toArray(array);
	}
	
	public static void register(final long number, final String name, final String[] aliases) {
		Integer programNumber = Long.valueOf(number).intValue();
		numberMap.put(name, programNumber);
		if (!nameMap.containsKey(programNumber)) { // keep the 1rst one
			nameMap.put(programNumber, name);
		}
		if (aliases != null) {
			for (String alias : aliases) {
				aliasesMap.put(alias, name);
				numberMap.put(alias, programNumber);
			}
		}
	}
	
	public static void register(final long number, final String name) {
		register(number, name, null);
	}
	
	public static String getName(final long number) {
		Integer key = Long.valueOf(number).intValue();
		if (nameMap.containsKey(key)) {
			return nameMap.get(key);
		}
		return /*"rpc#" + */ key.toString();
	}
	
	public static int getNumber(final String name) {
		if (numberMap.containsKey(name)) {
			return numberMap.get(name).intValue();
		}
		if (aliasesMap.containsKey(name)) {
			return getNumber(aliasesMap.get(name));
		}
		return 0;
	}
}
