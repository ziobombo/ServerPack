package it.fraguglia.transmission;

import nl.stil4m.transmission.http.HostConfiguration;

import java.net.URI;

public class RpcConfiguration implements HostConfiguration {

    private URI host;
    private String username;
    private String password;

    public URI getHost() {
        return host;
    }

    public void setHost(URI host) {
        this.host = host;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
