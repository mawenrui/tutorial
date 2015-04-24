    		OFPacketIn pi = (OFPacketIn)msg;
    		
    		// Parse the received packet		
            OFMatch match = new OFMatch();
            match.loadFromPacket(pi.getPacketData(), pi.getInPort());
           
            
    		// We only care about UDP packets
    		if (match.getDataLayerType() != Ethernet.TYPE_IPv4 && match.getDataLayerType() != Ethernet.TYPE_ARP) {
    			// Allow the next module to process this OpenFlow message
    		    return Command.CONTINUE;
    		}
    		
    		    		if (match.getDataLayerType() == Ethernet.TYPE_ARP) {
    			//generate arp reply
    			int dstHost = match.getNetworkDestination();
    			
    			String dHostIp = IPv4.fromIPv4Address(dstHost);
    			String dHostMac = hostIpMac.get(dHostIp);
    			
    			logger.info("Received an ARP request from the client");
            	handleARPRequest(sw, pi, cntx, dHostIp, dHostMac);
    			
    			
    		} else {
    			//System.out.println("Type is "+match.getDataLayerType());
    			
    			
    			
    			System.out.println("The packet is seen at sw "+sw.getId()+" inport "+pi.getInPort());
    			int sHost = match.getNetworkSource();
    			int dHost = match.getNetworkDestination();
    			
    			srcDstPair.put(sHost,dHost);
    			
    			Set<Map.Entry<Integer,Integer>> entry = srcDstPair.entrySet();
    			
    			System.out.println("The packet is seen at sw "+sw.getId()+" inport "+pi.getInPort());
    			System.out.println("SrcHost "+IPv4.fromIPv4Address(sHost)+" DstHost "+IPv4.fromIPv4Address(dHost));
    				
    			//get source and destination switch
    			long srcSwId = getSwDpid(IPv4.fromIPv4Address(sHost));
    			long dstSwId = getSwDpid(IPv4.fromIPv4Address(dHost));
    				
    			Route route = routing.getRoute(srcSwId, dstSwId, 0);
    				
    			logger.info("src sw"+srcSwId+" dest sw "+dstSwId);
    			System.out.println("The route is "+route);
    					
    			if(end[hostIndex.get(IPv4.fromIPv4Address(sHost))-1][hostIndex.get(IPv4.fromIPv4Address(dHost))-1]<0){
    				placeEndRule(sHost,dHost,route);
    				
    				}
    			
    		}
