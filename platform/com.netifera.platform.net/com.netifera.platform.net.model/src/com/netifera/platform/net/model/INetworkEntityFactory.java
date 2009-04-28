package com.netifera.platform.net.model;

import java.util.Map;

import com.netifera.platform.util.PortSet;
import com.netifera.platform.util.addresses.inet.InternetAddress;
import com.netifera.platform.util.addresses.inet.InternetNetblock;
import com.netifera.platform.util.locators.ISocketLocator;

public interface INetworkEntityFactory {

	InternetAddressEntity createAddress(long realm, long space, InternetAddress address);
	NetblockEntity createNetblock(long realm, long space, InternetNetblock netblock);

	void addOpenTCPPorts(long realm, long space, InternetAddress address, PortSet ports);
	void addOpenUDPPorts(long realm, long space, InternetAddress address, PortSet ports);

	ServiceEntity createService(long realm, long space, ISocketLocator locator, String serviceType, Map<String,String> info);
	ClientEntity createClient(long realm, long space, InternetAddress clientAddress, String serviceType, Map<String,String> info, ISocketLocator serviceLocator); // TODO add ISocketLocator serviceLocator -> HostEntity serverHost, i.e. `showmount -a`
	// TODO use Identity
	ClientServiceConnectionEntity createConnection(long space, ClientEntity client, ServiceEntity service, String identity); // identity: user, pubkey, ...

	void setOperatingSystem(long realm, long space, InternetAddress address, String os);

	UserEntity createUser(long realm, long space, InternetAddress address, String username);
	
	PasswordEntity createPassword(long realm, long space, ISocketLocator service, String password);
	UsernameAndPasswordEntity createUsernameAndPassword(long realm, long space, ISocketLocator service, String username, String password);
	// TODO remove 2 previous, replace by smth like serviceAddCredential(ISocketLocator service, Credential credential[user, userpw, privkey, ...])
	//CredentialEntity createCredential(long realm, long space, ISocketLocator service, Credential credential);
	//void addCredential(long realm, long space, ISocketLocator service, CredentialEntity credential);
}
