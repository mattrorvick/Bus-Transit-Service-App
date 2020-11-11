
let map;
let marker;


let contentString = "<h2> Vehicle#: " + vehicleNum + "</h2>";

function initMap() {

    
    

    map = new google.maps.Map(document.getElementById('map'), {
        center: { lat: parseFloat(busLocations[0].LATITUDE), lng: parseFloat(busLocations[0].LONGITUDE) },
        zoom: 15,
        scrollwheel: false
    });
    
    let image = {url: '/busicon.png', scaledSize: new google.maps.Size(40,40)};

    let userMarker = new google.maps.Marker({
        position: { lat: parseFloat(userLocation[i].lat), lng: parseFloat(userLocation[i].lng) },
        map: map,
        marker: marker,
    });

    for (i=0; i<busLocations.length; i++){
        

        let marker = new google.maps.Marker({
            position: { lat: parseFloat(busLocations[i].LATITUDE), lng: parseFloat(busLocations[i].LONGITUDE) },
            map: map,
            icon: image,
        });
    }


    google.maps.event.addListener(marker, 'click', function() {
        infoWindow.open(map, marker);
    });

    
}
