package serverResource;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class snmpProtocol {

	public static void main(String[] args) throws Exception {
		try {
		// Create transport mapping
		TransportMapping transport = new DefaultUdpTransportMapping();
		transport.listen();
		// Create SNMP session
		CommunityTarget target = new CommunityTarget();
		target.setCommunity(new OctetString("public"));
		target.setAddress(new UdpAddress("44.211.39.215/161")); // Agent address
		// Create SNMP request
		Snmp snmp = new Snmp(transport);
		PDU request = new PDU();
		request.setType(PDU.GET);
		request.add(new VariableBinding(new OID("1.3.6.1.4.1.25461.1.1.1.0"))); // OID to query
		// Send request and receive response
		ResponseEvent responseEvent = snmp.send(request, target);
		PDU response = responseEvent.getResponse();
		// Process response
		if (response != null) {
			VariableBinding vb = response.get(0);
			System.out.println("Value: " + vb.getVariable().toString());
		}}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
