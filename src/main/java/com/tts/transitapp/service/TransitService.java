package com.tts.transitapp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.tts.transitapp.model.Bus;
import com.tts.transitapp.model.BusComparator;
import com.tts.transitapp.model.BusRequest;
import com.tts.transitapp.model.DistanceResponse;
import com.tts.transitapp.model.GeocodingResponse;
import com.tts.transitapp.model.Location;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransitService {
    
    @Value("${transit_url}")
    public String transitUrl;

    @Value("${geocoding_url}")
    public String geocodingUrl;

    @Value("${distance_url}")
    public String distanceUrl;

    @Value("${api_key}")
    public String googleApiKey;

    




    private List<Bus> getBuses() {

        RestTemplate restTemplate = new RestTemplate();
        Bus[] buses = restTemplate.getForObject(transitUrl, Bus[].class);

        return Arrays.asList(buses);
    }

    private Location getCoordinates(String description) {

        // replace spaces with +'s (123 Main Street = 123+Main+Street)
        description = description.replace(" ", "+");
        //api url for finding street locations
        String url = geocodingUrl + description + "GA&key=" + googleApiKey;
        //system print url with street address
        System.out.println(url);
        //used to create applications that consume RESTful Web Services
        RestTemplate restTemplate = new RestTemplate();

        GeocodingResponse response = restTemplate.getForObject(url, GeocodingResponse.class);
        //parse info from data
        return response.results.get(0).geometry.location;
    }


    private double getDistance(Location origin, Location destination) {

        String url = distanceUrl + "origins=" + origin.lat + "," + origin.lng + "&destinations=" + destination.lat + "," + destination.lng + "&key=" + googleApiKey;

        System.out.println("Get distance formated URL: " + url);

        RestTemplate restTemplate = new RestTemplate();

        DistanceResponse response = restTemplate.getForObject(url, DistanceResponse.class);

        System.out.println("Response from distance matrix:" + response);

        return response.rows.get(0).elements.get(0).distance.value * 0.000621371;
    }

    public List<Bus> getNearbyBuses(BusRequest request) {
        
        // declaring variables
        //this allBuses variable, is the list of all the buses we get back from the getBuses method above
        List<Bus> allBuses = this.getBuses();
        //get location for person location with getCoordinates method, find address and city
        Location personLocation = this.getCoordinates(request.address + " " + request.city);

        System.out.println(personLocation);
        //Bus method create new ArrayList to find nearby buses for address search
        List<Bus> nearbyBuses = new ArrayList<>();
        //for loop bus model bus for all buses
        for (Bus bus : allBuses) {
            //new location for bus location
            Location busLocation = new Location();
            //is this changing lat and long variables?
            busLocation.lat = bus.LATITUDE;
            busLocation.lng = bus.LONGITUDE;
            //lat distance difference for user to bus.
            double latDistance = Double.parseDouble(busLocation.lat) - Double.parseDouble(personLocation.lat);
            //long distance difference for user to bus.
            double lngDistance = Double.parseDouble(busLocation.lng) - Double.parseDouble(personLocation.lng);
            //if statement calculating distance from person to nearest buses
            if(Math.abs(latDistance) <= 0.02 && Math.abs(lngDistance) <= 0.02) {
                double distance = getDistance(busLocation, personLocation);
                //if less than 1 mile I think it shows up as a nearby bus
                if(distance <= 1) {
                    bus.distance = (double) Math.round(distance * 100) / 100;
                    nearbyBuses.add(bus);
                }
            }
        }
        //sorting through nearby buses
        Collections.sort(nearbyBuses, new BusComparator());
        return nearbyBuses;
    }

    public Location getPersonLocation(BusRequest request) {

        Location personLocation = this.getCoordinates(request.address + " " + request.city);

        return personLocation;
    }
    

}