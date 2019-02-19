package com.bidomi.astrid.Controllers;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;


//@CrossOrigin(origins = "*", maxAge = 3600)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RestController
@RequestMapping(path = "/rest/geo")
public class GeoDataController {

//    private BasePointRepository markerRepository;

//    public GeoDataController(BasePointRepository markerRepository) {
//        this.markerRepository = markerRepository;
//    }

//    @PreAuthorize("hasAnyRole('USER')")
//    @GetMapping(path = "/all")
//    public @ResponseBody
//    List<> getAll(){
//        System.out.println("from GeoDataController");
//        return markerRepository.findAll();
//    }

    @PutMapping(path = "/create-markers")
    public @ResponseBody
    String createMarkers(@RequestBody String wktMarker) throws ParseException {
        try {
            Geometry geometry = new WKTReader().read(wktMarker);
        } catch (ParseException e) {
            return new String("Not a WKT string:" + wktMarker);
        }
        return "Ok";
    }
}
