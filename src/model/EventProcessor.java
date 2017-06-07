package model;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import event.CustomerPriceUpdate;
import event.Event;
import event.MailDelivery;
import event.TransportCostUpdate;
import event.TransportDiscontinued;

public class EventProcessor {
	
	private List<TransportCostUpdate> currentTcu;
	//from the list of all current tcu we can get a list of all routes.
	
	
	public EventProcessor(){
		this.currentTcu = new ArrayList<TransportCostUpdate>();
	}
	
	
	//
	public void processTCU(TransportCostUpdate tcu){
		//if tcu route is not in the list add it
		//otherwise just change it
		boolean updated = false;
		
		if(this.currentTcu.isEmpty()){
			this.currentTcu.add(tcu);
		}
		else{
			for(TransportCostUpdate transportCostUpdate : this.currentTcu){
				String origin = tcu.getOrigin();
				String destination = tcu.getDestination();
				String priority = tcu.getPriority();
				//contains the route then update
				if(origin.equals(transportCostUpdate.getOrigin()) && destination.equals(transportCostUpdate.getDestination()) && priority.equals(transportCostUpdate.getPriority())){
					transportCostUpdate = tcu;
					System.out.println("Replaced old tcu");
					updated = true;
					break;
				}
			}
			//if the tcu was not found in the current tcu list. add it to the end
			if(!updated){
				this.currentTcu.add(tcu);
			}
			
			
			
		}
	
	}
	
	public List<TransportCostUpdate> getCurrentTCU(){
		return this.currentTcu;
	}
	
	public boolean containsRoute(TransportCostUpdate tcu){
		
		for(TransportCostUpdate transportCostUpdate : this.currentTcu){
			String origin = tcu.getOrigin();
			String destination = tcu.getDestination();
			String priority = tcu.getPriority();
			if(origin.equals(transportCostUpdate.getOrigin()) && destination.equals(transportCostUpdate.getDestination()) && priority.equals(transportCostUpdate.getPriority())){
				System.out.println("current Tcu list contains tcu");
				return true;
			}
		}
		return false;
	}
	
	

	public Set<String> getLocationNames() {
		Set<String> locationNames = new HashSet<>();

		for(TransportCostUpdate tcu : currentTcu) {
			locationNames.add(tcu.getOrigin());
			locationNames.add(tcu.getDestination());
		}

		return locationNames;
	}
}
