


function initMap() {



    let image = {url: '/busicon.png', scaledSize: new google.maps.Size(50,50) };

    let map = new google.maps.Map(document.getElementById('map'), {
        center: { lat: parseFloat(busLocations[0].LATITUDE), lng: parseFloat(busLocations[0].LONGITUDE) }, 
        zoom: 15,
        scrollwheel: false
    });
    
    console.log(userLocation);

    let userMarker = new google.maps.Marker({
        position: { lat: parseFloat(userLocation.lat), lng: parseFloat(userLocation.lng) },
        map: map,
        
    });

    for (i=0; i<busLocations.length; i++){
        vehicleNum = busLocations[i].VEHICLE;
        let contentString = "<h2> Vehicle#: " + vehicleNum + "</h2>";
        let infoWindow = new google.maps.InfoWindow({
            content: contentString
        });

        console.log(busLocations);

        let marker = new google.maps.Marker({
            position: { lat: parseFloat(busLocations[i].LATITUDE), lng: parseFloat(busLocations[i].LONGITUDE) },
            map: map,
            
            icon: image,
        });

        marker.addListener("click", () => {
            infoWindow.open(map, marker);
        }); 
        
    }


    // google.maps.event.addListener(marker, 'click', function() {
    //     infoWindow.open(map, marker);
    // });
    

    
}
