package it.federicoII.indice;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.POS;

public class WordNetExtensions {

	public static NearestNeighborInWordnet getExtension(String concept) {

		NearestNeighborInWordnet nnWord = null;

		
		/*
		 * software
		 */
		if (concept.equalsIgnoreCase("on board software") || concept.equalsIgnoreCase("obsw")) {
			nnWord = new NearestNeighborInWordnet("software", 1.0);
		}

		if (concept.equalsIgnoreCase("ground software") || concept.equalsIgnoreCase("gsw")) {
			nnWord = new NearestNeighborInWordnet("software", 2.0);
		}
		
		
		
		
		/*
		 * report
		 */
		if (concept.equalsIgnoreCase("health status report")  || concept.equalsIgnoreCase("report health status") ) {
			nnWord = new NearestNeighborInWordnet("report", 1.0);
		}
		
		
		
		
		
		/*
		 * phase
		 */
		if (concept.equalsIgnoreCase("Mission phase") ) {
			nnWord = new NearestNeighborInWordnet("phase", 1.0);
		}
		
		/*
		 * per i prossimi la distanza è 2.0 perche in realtà sono delle "mission phase"
		 */
		if (concept.equalsIgnoreCase("umbilical") || concept.equalsIgnoreCase("umbilical phase") ) {
			nnWord = new NearestNeighborInWordnet("phase", 1.0);
		}

		if (concept.equalsIgnoreCase("descend") || concept.equalsIgnoreCase("descent") ||
				concept.equalsIgnoreCase("descend phase") || concept.equalsIgnoreCase("descent phase") ) {
			nnWord = new NearestNeighborInWordnet("phase", 1.5);
		}
		
		if (concept.equalsIgnoreCase("ascend") || concept.equalsIgnoreCase("ascent") ||
				concept.equalsIgnoreCase("ascend phase") || concept.equalsIgnoreCase("ascent phase")) {
			nnWord = new NearestNeighborInWordnet("phase", 2.0);
		}

		if (concept.equalsIgnoreCase("flight") || concept.equalsIgnoreCase("flight phase")) {
			nnWord = new NearestNeighborInWordnet("phase", 2.5);
		}

		if (concept.equalsIgnoreCase("floatation") || concept.equalsIgnoreCase("floatation phase")) {
			nnWord = new NearestNeighborInWordnet("phase", 3.0);
		}
		
		if (concept.equalsIgnoreCase("stand-by") || concept.equalsIgnoreCase("stand-by phase") ) {
			nnWord = new NearestNeighborInWordnet("phase", 3.5);
		}
		
		if (concept.equalsIgnoreCase("Pre-launch") || concept.equalsIgnoreCase("Pre-launch phase")) {
			nnWord = new NearestNeighborInWordnet("phase", 4.0);
		}
		
		if (concept.equalsIgnoreCase("start-up") || concept.equalsIgnoreCase("start-up phase") ) {
			nnWord = new NearestNeighborInWordnet("phase", 4.5);
		}
		
		
		
		
		/* 
		 * command
		 */
		if (concept.equalsIgnoreCase("FTS activation command") || concept.equalsIgnoreCase("FTS activation commands")) {
			nnWord = new NearestNeighborInWordnet("command", 1.0);
		}
		
		if (concept.equalsIgnoreCase("set up commands") || concept.equalsIgnoreCase("set_up_command")) {
			nnWord = new NearestNeighborInWordnet("command", 1.0);
		}
		
		//(verificare da qui)
		if (concept.equalsIgnoreCase("activation sequence") ) {
			nnWord = new NearestNeighborInWordnet("command", 1.2);
		}
		
		if (concept.equalsIgnoreCase("power on command") ) {
			nnWord = new NearestNeighborInWordnet("data", 1.4);
		}
		
		if (concept.equalsIgnoreCase("shouting sequence") ) {
			nnWord = new NearestNeighborInWordnet("command", 1.6);
		}
		
		if (concept.equalsIgnoreCase("self-test execution command") ) {
			nnWord = new NearestNeighborInWordnet("command", 1.8);
		}
		
		if (concept.equalsIgnoreCase("self-test results acquisition command") ) {
			nnWord = new NearestNeighborInWordnet("command", 2.0);
		}
		
		if (concept.equalsIgnoreCase("initialization command") ) {
			nnWord = new NearestNeighborInWordnet("command", 2.2);
		}
		
		if (concept.equalsIgnoreCase("configuration setting command") ) {
			nnWord = new NearestNeighborInWordnet("command", 2.4);
		}
		
		if (concept.equalsIgnoreCase("configuration status acquisition command") ) {
			nnWord = new NearestNeighborInWordnet("command", 2.6);
		}
		
		if (concept.equalsIgnoreCase("equipment command") ) {
			nnWord = new NearestNeighborInWordnet("command", 2.8);
		}
		//(fino a qui)
		
		if (concept.equalsIgnoreCase("Telecommand") ) {
			nnWord = new NearestNeighborInWordnet("command", 1.2);
		}
		
		if (concept.equalsIgnoreCase("mission command") || concept.equalsIgnoreCase("mission-command")) {
			nnWord = new NearestNeighborInWordnet("command", 1.4);
		}
		
		if (concept.equalsIgnoreCase("ground command") || concept.equalsIgnoreCase("set_up_command")) {
			nnWord = new NearestNeighborInWordnet("command", 1.5);
		}
		
		if (concept.equalsIgnoreCase("surface_commands") || concept.equalsIgnoreCase("surface commands")) {
			nnWord = new NearestNeighborInWordnet("command", 1.7);
		}
		
		if (concept.equalsIgnoreCase("landg_commands") || concept.equalsIgnoreCase("landg commands")) {
			nnWord = new NearestNeighborInWordnet("command", 1.9);
		}
		
		if (concept.equalsIgnoreCase("Aebr_command") || concept.equalsIgnoreCase("Aebr command")) {
			nnWord = new NearestNeighborInWordnet("command", 2.1);
		}
		
		if (concept.equalsIgnoreCase("rcs_command") || concept.equalsIgnoreCase("rcs_command")) {
			nnWord = new NearestNeighborInWordnet("command", 2.3);
		}
		
		if (concept.equalsIgnoreCase("ground command") || concept.equalsIgnoreCase("set_up_command")) {
			nnWord = new NearestNeighborInWordnet("command", 2.5);
		}
		
		if (concept.equalsIgnoreCase("ground command") || concept.equalsIgnoreCase("set_up_command")) {
			nnWord = new NearestNeighborInWordnet("command", 2.7);
		}
		
		
		
		
	
		
		/* 
		 * equipment
		 */
		if (concept.equalsIgnoreCase("Vehicle equipment") ) {
			nnWord = new NearestNeighborInWordnet("equipment", 1.0);
		}
		/*
		 * per i prossimi la distanza è 2.0 perche in realtà sono dei "Vehicle equipment"
		 */
		if (concept.equalsIgnoreCase("Power supply") || concept.equalsIgnoreCase("Power-supply")) {
			nnWord = new NearestNeighborInWordnet("equipment", 1.0);
		}
		
		if (concept.equalsIgnoreCase("Payload") ) {
			nnWord = new NearestNeighborInWordnet("equipment", 1.1);
		}
		
		if (concept.equalsIgnoreCase("ADS") ) {
			nnWord = new NearestNeighborInWordnet("equipment", 1.1);
		}
		
		if (concept.equalsIgnoreCase("Transponder") ) {
			nnWord = new NearestNeighborInWordnet("equipment", 1.2);
		}
		
		if (concept.equalsIgnoreCase("Transmitter") ) {
			nnWord = new NearestNeighborInWordnet("equipment", 1.3);
		}
		

		if (concept.equalsIgnoreCase("Receiver") ) {
			nnWord = new NearestNeighborInWordnet("equipment", 1.4);
		}
		

		if (concept.equalsIgnoreCase("Power amplifier") || concept.equalsIgnoreCase("Power-amplifier") ) {
			nnWord = new NearestNeighborInWordnet("equipment", 1.5);
		}
		

		if (concept.equalsIgnoreCase("RTC") ) {
			nnWord = new NearestNeighborInWordnet("equipment", 1.6);
		}
		

		if (concept.equalsIgnoreCase("GPS") ) {
			nnWord = new NearestNeighborInWordnet("equipment", 1.7);
		}
		

		if (concept.equalsIgnoreCase("Radar") ) {
			nnWord = new NearestNeighborInWordnet("equipment", 1.8);
		}
		

		if (concept.equalsIgnoreCase("laser") ) {
			nnWord = new NearestNeighborInWordnet("equipment", 1.9);
		}
		
		if (concept.equalsIgnoreCase("AVUM") ) {
			nnWord = new NearestNeighborInWordnet("equipment", 1.10);
		}
		

		if (concept.equalsIgnoreCase("GNC equipment") ) {
			nnWord = new NearestNeighborInWordnet("equipment", 2.0);
		}
		
		/*
		 * a 3.0 perche sono GNC equipment
		 */
		if (concept.equalsIgnoreCase("Sensor1") ) {
			nnWord = new NearestNeighborInWordnet("equipment", 2.1);
		}

		if (concept.equalsIgnoreCase("ADS") ) {
			nnWord = new NearestNeighborInWordnet("equipment", 2.2);
		}

		if (concept.equalsIgnoreCase("Actuator") ) {
			nnWord = new NearestNeighborInWordnet("equipment", 2.3);
		}

		if (concept.equalsIgnoreCase("HYSY") ) {
			nnWord = new NearestNeighborInWordnet("equipment", 2.4);
		}

		if (concept.equalsIgnoreCase("AVUM") ) {
			nnWord = new NearestNeighborInWordnet("equipment", 2.5);
		}

		/*
		 * a 3.0 perchè è un veichle equipment
		 */
		if (concept.equalsIgnoreCase("FDR") ) {
			nnWord = new NearestNeighborInWordnet("equipment", 2.7);
		}
		
		
		
		
	
		
		/*
		 * process
		 */
		if (concept.equalsIgnoreCase("destination process") ) {
			nnWord = new NearestNeighborInWordnet("process", 1.0);
		}
		
		if (concept.equalsIgnoreCase("apid") ) {
			nnWord = new NearestNeighborInWordnet("process", 2.0);
		}
		
		
		
		
		/*
		 * list
		 */
		if (concept.equalsIgnoreCase("tc_list") ) {
			nnWord = new NearestNeighborInWordnet("list", 1.0);
		}
		
		/*
		 * 2.0 perchè sono tc_list
		 */
		if (concept.equalsIgnoreCase("tc_list") ) {
			nnWord = new NearestNeighborInWordnet("list", 1.1);
		}
		
		if (concept.equalsIgnoreCase("Power-on") ) {
			nnWord = new NearestNeighborInWordnet("list", 1.2);
		}
		
		if (concept.equalsIgnoreCase("Power-off") ) {
			nnWord = new NearestNeighborInWordnet("list", 1.3);
		}
		
		if (concept.equalsIgnoreCase("Equipment initialization") ) {
			nnWord = new NearestNeighborInWordnet("list", 1.4);
		}
		
		if (concept.equalsIgnoreCase("Equipment configuration setting") ) {
			nnWord = new NearestNeighborInWordnet("list", 1.5);
		}
		
		if (concept.equalsIgnoreCase("Equipment configuration acquisition") ) {
			nnWord = new NearestNeighborInWordnet("list", 1.6);
		}
		
		
		if (concept.equalsIgnoreCase("critical_tc_list") ) {
			nnWord = new NearestNeighborInWordnet("list", 1.7);
		}
		
		if (concept.equalsIgnoreCase("telemetry_phase_list") ) {
			nnWord = new NearestNeighborInWordnet("list", 1.8);
		}
		/*
		 * 3.0 perchè sono critical_tc_list
		 */
		if (concept.equalsIgnoreCase("FTS") ) {
			nnWord = new NearestNeighborInWordnet("list", 1.9);
		}
		
		if (concept.equalsIgnoreCase("Shout-down") ) {
			nnWord = new NearestNeighborInWordnet("list", 2.0);
		}
		
		if (concept.equalsIgnoreCase("Reboot") ) {
			nnWord = new NearestNeighborInWordnet("list", 2.1);
		}
		/*
		 * 3.0 perchè sono telemetry_phase_list
		 */
		if (concept.equalsIgnoreCase("RTU packet") ) {
			nnWord = new NearestNeighborInWordnet("list", 2.2);
		}
		
		if (concept.equalsIgnoreCase("Payload packet") ) {
			nnWord = new NearestNeighborInWordnet("list", 2.3);
		}
		
		if (concept.equalsIgnoreCase("Software packet") ) {
			nnWord = new NearestNeighborInWordnet("list", 2.4);
		}
		
		if (concept.equalsIgnoreCase("GNC packet") ) {
			nnWord = new NearestNeighborInWordnet("list", 2.5);
		}

		
		
		
		/*
		 * packet
		 */
		if (concept.equalsIgnoreCase("housekeeping packet") ) {
			nnWord = new NearestNeighborInWordnet("packet", 1.0);
		}
		
		if (concept.equalsIgnoreCase("telemetry packet") || concept.equalsIgnoreCase("telemetry packets") ) {
			nnWord = new NearestNeighborInWordnet("packet", 2.0);
		}
		
		/*
		 * 2.0 perche sono housekeeping packet
		 */
		if (concept.equalsIgnoreCase("Hk telemetry packet") ) {
			nnWord = new NearestNeighborInWordnet("packet", 2.2);
		}
		
		if (concept.equalsIgnoreCase("CCSDS packet") ) {
			nnWord = new NearestNeighborInWordnet("packet", 2.4);
		}
	
		
		
		/*
		 * data
		 */
		if (concept.equalsIgnoreCase("hk data") ) {
			nnWord = new NearestNeighborInWordnet("data", 1.0);
		}
		
		if (concept.equalsIgnoreCase("mission data") ) {
			nnWord = new NearestNeighborInWordnet("data", 1.0);
		}
		
		//(Verificare da qui!)
		if (concept.equalsIgnoreCase("on board software status") ) {
			nnWord = new NearestNeighborInWordnet("data", 1.1);
		}
		
		if (concept.equalsIgnoreCase("calculated checksum") ) {
			nnWord = new NearestNeighborInWordnet("data", 1.2);
		}
		
		if (concept.equalsIgnoreCase("telecommand packet error control field") ) {
			nnWord = new NearestNeighborInWordnet("data", 1.3);
		}
		
		
		if (concept.equalsIgnoreCase("telecommand packet error control field") ) {
			nnWord = new NearestNeighborInWordnet("data", 1.4);
		}
		
		if (concept.equalsIgnoreCase("valid value") ) {
			nnWord = new NearestNeighborInWordnet("data", 1.5);
		}
		//(fino a qui)
		/*
		 * 2.0 perche sono hk data
		 */
		if (concept.equalsIgnoreCase("Power supply data") ) {
			nnWord = new NearestNeighborInWordnet("data", 1.6);
		}
		
		if (concept.equalsIgnoreCase("Avionic Bus data") ) {
			nnWord = new NearestNeighborInWordnet("data", 1.7);
		}
		
		if (concept.equalsIgnoreCase("RTC data") ) {
			nnWord = new NearestNeighborInWordnet("data", 1.8);
		}
		
		if (concept.equalsIgnoreCase("gnc equipment data") ) {
			nnWord = new NearestNeighborInWordnet("data", 4.0);
		}
		
		if (concept.equalsIgnoreCase("payload data") ) {
			nnWord = new NearestNeighborInWordnet("data", 1.9);
		}
		
		if (concept.equalsIgnoreCase("FDR data") ) {
			nnWord = new NearestNeighborInWordnet("data", 2.0);
		}
		
		if (concept.equalsIgnoreCase("structure data") ) {
			nnWord = new NearestNeighborInWordnet("data", 2.1);
		}
		
		if (concept.equalsIgnoreCase("transponder data") ) {
			nnWord = new NearestNeighborInWordnet("data", 2.2);
		}
		
		if (concept.equalsIgnoreCase("mon_list_data") ) {
			nnWord = new NearestNeighborInWordnet("data", 2.3);
		}
		/*
		 * 2.0 perche sono mission data
		 */
		if (concept.equalsIgnoreCase("ads_mss_data") ) {
			nnWord = new NearestNeighborInWordnet("data", 2.4);
		}
		
		if (concept.equalsIgnoreCase("gps_mss_data") ) {
			nnWord = new NearestNeighborInWordnet("data", 2.5);
		}

		if (concept.equalsIgnoreCase("ins_mss_data") ) {
			nnWord = new NearestNeighborInWordnet("data", 2.6);
		}

		if (concept.equalsIgnoreCase("aerbr_mss_data") ) {
			nnWord = new NearestNeighborInWordnet("data", 2.7);
		}

		if (concept.equalsIgnoreCase("rcs_mss_data") ) {
			nnWord = new NearestNeighborInWordnet("data", 2.8);
		}

		if (concept.equalsIgnoreCase("ctrsurf_mss_data") ) {
			nnWord = new NearestNeighborInWordnet("data", 2.9);
		}

		if (concept.equalsIgnoreCase("landg_mss_data") ) {
			nnWord = new NearestNeighborInWordnet("data", 3.0);
		}

		if (concept.equalsIgnoreCase("radar_mss_data") ) {
			nnWord = new NearestNeighborInWordnet("data", 3.1);
		}

		if (concept.equalsIgnoreCase("rlaser_mss_data") ) {
			nnWord = new NearestNeighborInWordnet("data", 3.2);
		}

		if (concept.equalsIgnoreCase("avum_mss_data") ) {
			nnWord = new NearestNeighborInWordnet("data", 3.3);
		}

		
		
		
		/* 
		 * produce
		 */
		if (concept.equalsIgnoreCase("produce_IN") ) {
			nnWord = new NearestNeighborInWordnet("produce", 2.0);
		}
		
		
		
		/* 
		 * disable
		 */
		if (concept.equalsIgnoreCase("disable_IN") ) {
			nnWord = new NearestNeighborInWordnet("disable", 1.0);
		}
		
		
		
		/*
		 * unit (verificare!)
		 */
		
		if (concept.equalsIgnoreCase("remote terminal unit") ) {
			nnWord = new NearestNeighborInWordnet("unit", 2.0);
		}
		
		
		
		
		/*
		 * send
		 */
		
		if (concept.equalsIgnoreCase("send_TO") ) {
			nnWord = new NearestNeighborInWordnet("send", 1.0);
		}
		
		if (concept.equalsIgnoreCase("send_IN") ) {
			nnWord = new NearestNeighborInWordnet("send", 1.1);
		}
		
		if (concept.equalsIgnoreCase("send_by") ) {
			nnWord = new NearestNeighborInWordnet("send", 1.2);
		}
		
		if (concept.equalsIgnoreCase("send_when") ) {
			nnWord = new NearestNeighborInWordnet("send", 1.3);
		}
		
		if (concept.equalsIgnoreCase("send_within") ) {
			nnWord = new NearestNeighborInWordnet("send", 1.4);
		}
		
		
		
		/*
		 * amplifier (verificare)
		 */
		if (concept.equalsIgnoreCase("power amplifier") ) {
			nnWord = new NearestNeighborInWordnet("amplifier", 1.0);
		}
		
				
		
		/*
		 * exit
		 */
		
		if (concept.equalsIgnoreCase("exit_FROM") ) {
			nnWord = new NearestNeighborInWordnet("exit", 1.0);
		}
		
		
		
		
		/*
		 * place (verificare!)
		 */
		if (concept.equalsIgnoreCase("segregated area") ) {
			nnWord = new NearestNeighborInWordnet("place", 2.0);
		}
		
		if (concept.equalsIgnoreCase("ground segment") || concept.equalsIgnoreCase("ground") ) {
			nnWord = new NearestNeighborInWordnet("place", 3.0);
		}
		
		
		
		/*
		 * block (verificare!)
		 */
		if (concept.equalsIgnoreCase("power off") ) {
			nnWord = new NearestNeighborInWordnet("block", 1.0);
		}
		
		if (concept.equalsIgnoreCase("not send") ) {
			nnWord = new NearestNeighborInWordnet("block", 2.0);
		}
		
		
		
		
		/*
		 * receive
		 */
		if (concept.equalsIgnoreCase("receive_DA") || concept.equalsIgnoreCase("receive_FROM") ) {
			nnWord = new NearestNeighborInWordnet("receive", 1.0);
		}
		
		
		
		/*
		 * wait
		 */
		if (concept.equalsIgnoreCase("wait_DA") || concept.equalsIgnoreCase("wait_FROM") ) {
			nnWord = new NearestNeighborInWordnet("wait", 1.0);
		}
		
		
		
		/*
		 * function
		 */
		if (concept.equalsIgnoreCase("assigned function") || concept.equalsIgnoreCase("wait_FROM") ) {
			nnWord = new NearestNeighborInWordnet("function", 1.0);
		}
		
		if (concept.equalsIgnoreCase("telecommand checksum") ) {
			nnWord = new NearestNeighborInWordnet("function", 1.5);
		}
		
		
		/*
		 * perform
		 */
		if (concept.equalsIgnoreCase("perform_in") || concept.equalsIgnoreCase("wait_FROM") ) {
			nnWord = new NearestNeighborInWordnet("perform", 1.4);
		}
		
		if (concept.equalsIgnoreCase("perform_within") || concept.equalsIgnoreCase("wait_FROM") ) {
			nnWord = new NearestNeighborInWordnet("perform", 1.8);
		}
		
		
		/*
		 * timeline
		 */
		if (concept.equalsIgnoreCase("mission timeline") ) {
			nnWord = new NearestNeighborInWordnet("timeline", 1.0);
		}
		
		
		/*
		 * decode
		 */
		if (concept.equalsIgnoreCase("decode_in") ) {
			nnWord = new NearestNeighborInWordnet("decode", 1.3);
		}
		
		if (concept.equalsIgnoreCase("decode_within") ) {
			nnWord = new NearestNeighborInWordnet("decode", 1.6);
		}
		
				
		/*
		 * value
		 */
		if (concept.equalsIgnoreCase("header allowed  value") ) {
			nnWord = new NearestNeighborInWordnet("value", 1.0);
		}
		
		
		
		/*
		 * check
		 */
		if (concept.equalsIgnoreCase("check_within") ) {
			nnWord = new NearestNeighborInWordnet("check", 1.4);
		}
		
		if (concept.equalsIgnoreCase("check_when") ) {
			nnWord = new NearestNeighborInWordnet("check", 1.8);
		}
		
		
		
		//VERIFICARE IL NOT
		if (concept.equalsIgnoreCase("passed telecommands") || concept.equalsIgnoreCase("not passed telecommands")) {
			nnWord = new NearestNeighborInWordnet("command", 1.0);
		}
		
		
		/*
		 * count
		 */
		if (concept.equalsIgnoreCase("count_in") ) {
			nnWord = new NearestNeighborInWordnet("count", 1.4);
		}
		
		if (concept.equalsIgnoreCase("count_within") ) {
			nnWord = new NearestNeighborInWordnet("count", 1.8);
		}
		
		/*
		 * protocol
		 */
		if (concept.equalsIgnoreCase("arm and fire protocol") ) {
			nnWord = new NearestNeighborInWordnet("protocol", 1.0);
		}
		
		
		/*
		 * encode
		 */
		if (concept.equalsIgnoreCase("encode_in") ) {
			nnWord = new NearestNeighborInWordnet("encode", 1.4);
		}
		
		if (concept.equalsIgnoreCase("encode_within") ) {
			nnWord = new NearestNeighborInWordnet("encode", 1.8);
		}
		
		
		//VERIFICARE!
		if (concept.equalsIgnoreCase("telemetry_phase_list") ) {
			nnWord = new NearestNeighborInWordnet("phase", 1.0);
		}
		
		
		/*
		 * event
		 */
		//VERIFICARE!
		if (concept.equalsIgnoreCase("event telemetry packet") ) {
			nnWord = new NearestNeighborInWordnet("event", 1.5);
		}

		
		/*
		 * event
		 */
		//VERIFICARE!
		if (concept.equalsIgnoreCase("period 1s") ) {
			nnWord = new NearestNeighborInWordnet("time", 1.0);
		}
		
		if (concept.equalsIgnoreCase("period 0.5s") ) {
			nnWord = new NearestNeighborInWordnet("time", 1.0);
		}

		if (concept.equalsIgnoreCase("period 0.1s") ) {
			nnWord = new NearestNeighborInWordnet("time", 1.0);
		}
		
		if (concept.equalsIgnoreCase("0.1s") ) {
			nnWord = new NearestNeighborInWordnet("time", 1.0);
		}
		
		if (concept.equalsIgnoreCase("period 1 s") ) {
			nnWord = new NearestNeighborInWordnet("time", 1.0);
		}
		
		if (concept.equalsIgnoreCase("period 10 s") ) {
			nnWord = new NearestNeighborInWordnet("time", 1.0);
		}
		
		
		
		/*
		 * acquire
		 */
		if (concept.equalsIgnoreCase("acquire_from") ) {
			nnWord = new NearestNeighborInWordnet("acquire", 1.3);
		}
		
		if (concept.equalsIgnoreCase("acquire_when") ) {
			nnWord = new NearestNeighborInWordnet("acquire", 1.6);
		}
		
		if (concept.equalsIgnoreCase("acquire_in") ) {
			nnWord = new NearestNeighborInWordnet("acquire", 1.9);
		}
		
		
		/*
		 * component
		 */
		//VERIFICARE!
		if (concept.equalsIgnoreCase("avionic buses") ) {
			nnWord = new NearestNeighborInWordnet("component", 1.0);
		}
		
		
		
		/*
		 * resource
		 */
		//VERIFICARE!
		if (concept.equalsIgnoreCase("FIFO used space") ) {
			nnWord = new NearestNeighborInWordnet("resource", 1.0);
		}

		
		/*
		 * status
		 */
		//VERIFICARE!
		if (concept.equalsIgnoreCase("starting status") ) {
			nnWord = new NearestNeighborInWordnet("status", 1.3);
		}

		if (concept.equalsIgnoreCase("on") ) {
			nnWord = new NearestNeighborInWordnet("status", 1.6);
		}
		
		
		
		/*
		 * gear
		 */
		//VERIFICARE!
		if (concept.equalsIgnoreCase("landing gear") ) {
			nnWord = new NearestNeighborInWordnet("gear", 1.0);
		}
		
		
		/*
		 * brake
		 */
		//VERIFICARE!
		if (concept.equalsIgnoreCase("aerobarke") ) {
			nnWord = new NearestNeighborInWordnet("brake", 1.0);
		}
		
		
		/*
		 * value
		 */
		//VERIFICARE!
		if (concept.equalsIgnoreCase("allowed limit") ) {
			nnWord = new NearestNeighborInWordnet("value", 1.0);
		}
		
		
		/*
		 * delete
		 */
		if (concept.equalsIgnoreCase("delete_within") ) {
			nnWord = new NearestNeighborInWordnet("delete", 1.0);
		}
		
		
		/*
		 * create
		 */
		if (concept.equalsIgnoreCase("create_where") ) {
			nnWord = new NearestNeighborInWordnet("create", 1.0);
		}
		
		
		/*
		 * store
		 */
		//VERIFICARE
		if (concept.equalsIgnoreCase("not_delete") ) {
			nnWord = new NearestNeighborInWordnet("store", 1.0);
		}
		
		if (concept.equalsIgnoreCase("store_where") ) {
			nnWord = new NearestNeighborInWordnet("store", 1.3);
		}
		
		if (concept.equalsIgnoreCase("store_within") ) {
			nnWord = new NearestNeighborInWordnet("store", 1.6);
		}

		
		
		
		if (concept.equalsIgnoreCase("transponder hk data") ) {
			nnWord = new NearestNeighborInWordnet("data", 2.0);
		}
		
		if (concept.equalsIgnoreCase("structure hk data") ) {
			nnWord = new NearestNeighborInWordnet("data", 2.2);
		}
		
		if (concept.equalsIgnoreCase("structure mission data") ) {
			nnWord = new NearestNeighborInWordnet("data", 2.4);
		}
		
		if (concept.equalsIgnoreCase("power supply hk data") ) {
			nnWord = new NearestNeighborInWordnet("data", 2.6);
		}
		
		if (concept.equalsIgnoreCase("avionic bus hk data") ) {
			nnWord = new NearestNeighborInWordnet("data", 2.8);
		}
		
		if (concept.equalsIgnoreCase("GNC hk data") ) {
			nnWord = new NearestNeighborInWordnet("data", 3.0);
		}
		
		
		
		/*
		 * greater: deve essere verbo!!
		 */
		if (concept.equalsIgnoreCase("is greater") ) {
			nnWord = new NearestNeighborInWordnet("exceed", 1.0);
		}
		
		
		
		/*
		 * equal
		 */
		if (concept.equalsIgnoreCase("is equal") ) {
			nnWord = new NearestNeighborInWordnet("equal", 1.0);
		}
		
		
		
		/*
		 * less
		 */
		if (concept.equalsIgnoreCase("is less") ) {
			nnWord = new NearestNeighborInWordnet("undergo", 1.0);
		}
		
		
		/*
		 * be
		 */
		if (concept.equalsIgnoreCase("is") ) {
			nnWord = new NearestNeighborInWordnet("be", 1.0);
		}
		
		if (concept.equalsIgnoreCase("is_in") ) {
			nnWord = new NearestNeighborInWordnet("be", 1.0);
		}
		
		
		
		if (concept.equalsIgnoreCase("the power on command") ) {
			nnWord = new NearestNeighborInWordnet("command", 1.2);
		}
		
		if (concept.equalsIgnoreCase("power off command") ) {
			nnWord = new NearestNeighborInWordnet("command", 1.4);
		}
		
		if (concept.equalsIgnoreCase("telecommands") ) {
			nnWord = new NearestNeighborInWordnet("command", 1.6);
		}
		
		
		if (concept.equalsIgnoreCase("SSMM") ) {
			nnWord = new NearestNeighborInWordnet("equipment", 1.0);
		}
		
		if (concept.equalsIgnoreCase("1s") ) {
			nnWord = new NearestNeighborInWordnet("time", 1.0);
		}
		
		if (concept.equalsIgnoreCase("0.3s") ) {
			nnWord = new NearestNeighborInWordnet("time", 1.0);
		}
		
		if (concept.equalsIgnoreCase("0.2s") ) {
			nnWord = new NearestNeighborInWordnet("time", 1.0);
		}
		
		if (concept.equalsIgnoreCase("0.3") ) {
			nnWord = new NearestNeighborInWordnet("value", 1.0);
		}
		
		if (concept.equalsIgnoreCase("period 0.01s") ) {
			nnWord = new NearestNeighborInWordnet("time", 1.0);
		}
		
		
		
		
		/*
		 * have
		 */
		if (concept.equalsIgnoreCase("has") ) {
			nnWord = new NearestNeighborInWordnet("have", 1.0);
		}
		
		
		
		/*
		 * calculate
		 */
		if (concept.equalsIgnoreCase("calculate_within") ) {
			nnWord = new NearestNeighborInWordnet("calculate", 1.0);
		}
		
		
		
		//VERIFICARE
		if (concept.equalsIgnoreCase("not_passed_telecommands") ) {
			nnWord = new NearestNeighborInWordnet("data", 1.0);
		}
		
		
		if (concept.equalsIgnoreCase("OB time") ) {
			nnWord = new NearestNeighborInWordnet("data", 1.2);
		}
		
		if (concept.equalsIgnoreCase("memory disk used") ) {
			nnWord = new NearestNeighborInWordnet("resource", 1.4);
		}
		
		if (concept.equalsIgnoreCase("storage memory") ) {
			nnWord = new NearestNeighborInWordnet("equipment", 1.6);
		}
		
		if (concept.equalsIgnoreCase("mon_list") ) {
			nnWord = new NearestNeighborInWordnet("list", 1.8);
		}
		
		if (concept.equalsIgnoreCase("evt_list") ) {
			nnWord = new NearestNeighborInWordnet("list", 2.0);
		}
		
		if (concept.equalsIgnoreCase("mon_cross_list") ) {
			nnWord = new NearestNeighborInWordnet("list", 2.2);
		}
		
		if (concept.equalsIgnoreCase("st_list") ) {
			nnWord = new NearestNeighborInWordnet("list", 2.4);
		}
		
		if (concept.equalsIgnoreCase("tm_list") ) {
			nnWord = new NearestNeighborInWordnet("list", 2.6);
		}
		
		if (concept.equalsIgnoreCase("start-up command") ) {
			nnWord = new NearestNeighborInWordnet("command", 2.8);
		}
		
		if (concept.equalsIgnoreCase("NVRAM") ) {
			nnWord = new NearestNeighborInWordnet("equipment", 3.2);
		}
		
		if (concept.equalsIgnoreCase("aebr_mss_data") ) {
			nnWord = new NearestNeighborInWordnet("data", 3.4);
		}
		
		if (concept.equalsIgnoreCase("laser_mss_data") ) {
			nnWord = new NearestNeighborInWordnet("data", 3.6);
		}
		
		if (concept.equalsIgnoreCase("surface_command") ) {
			nnWord = new NearestNeighborInWordnet("command", 3.8);
		}
		
		if (concept.equalsIgnoreCase("control surfaces") ) {
			nnWord = new NearestNeighborInWordnet("equipment", 4.2);
		}
		
		if (concept.equalsIgnoreCase("landg_command") ) {
			nnWord = new NearestNeighborInWordnet("command", 4.4);
		}
		
		if (concept.equalsIgnoreCase("rcs") ) {
			nnWord = new NearestNeighborInWordnet("equipment", 4.6);
		}
		
		if (concept.equalsIgnoreCase("mon_list") ) {
			nnWord = new NearestNeighborInWordnet("list", 4.8);
		}
				
		if (concept.equalsIgnoreCase("mon_list data") ) {
			nnWord = new NearestNeighborInWordnet("data", 5.2);
		}
		
		if (concept.equalsIgnoreCase("mon_list value") ) {
			nnWord = new NearestNeighborInWordnet("value", 5.4);
		}
		
		if (concept.equalsIgnoreCase("period 0.1 s") ) {
			nnWord = new NearestNeighborInWordnet("time", 5.6);
		}
		
		if (concept.equalsIgnoreCase("payload hk data") ) {
			nnWord = new NearestNeighborInWordnet("data", 5.8);
		}
		
		if (concept.equalsIgnoreCase("fdr hk data") ) {
			nnWord = new NearestNeighborInWordnet("data", 6.0);
		}
		
		if (concept.equalsIgnoreCase("storing") ) {
			nnWord = new NearestNeighborInWordnet("store", 6.4);
		}
		
		if (concept.equalsIgnoreCase("exceeds") ) {
			nnWord = new NearestNeighborInWordnet("exceed", 6.6);
		}
		
		
		
		
		
		
		
		
		
		 		
		
		if ( nnWord == null) {
			System.out.println("attenzione: " + concept + " non è stato inserito nell'ontologia" );
		}
		return nnWord;
	} 

	public static NearestNeighborInWordnet getNearestInWordNet(String concept, String pos, IDictionary dict) {

		NearestNeighborInWordnet nnWord = null;
		
		/*
		if (concept.equalsIgnoreCase("send") || concept.equalsIgnoreCase("disable")) {
			System.out.println("ok");
		}
		*/

		if ( ! belongToWordNet(concept, pos, dict)) {

			nnWord = WordNetExtensions.getExtension(concept);

		} else {

			nnWord = new NearestNeighborInWordnet(concept, 0.0);
		}
		return nnWord;
	}
	
	public static boolean belongToWordNet(String concept, String pos, IDictionary dict) {

		IIndexWord	word	=	null;

		// get the WordNet words in *any* POS
		if(pos.equalsIgnoreCase("n")) {
			
			word = dict.getIndexWord(concept, POS.NOUN);

		}
		
		if(pos.equalsIgnoreCase("v")) {
			
			word = dict.getIndexWord(concept, POS.VERB);

		}

		if(word == null) {
			
			return false;
		}
		return true;
	}
}
