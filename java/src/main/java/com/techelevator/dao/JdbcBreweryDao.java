package com.techelevator.dao;

import com.techelevator.model.Beer;
import com.techelevator.model.Brewery;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcBreweryDao implements BreweryDao{

    private JdbcTemplate jdbcTemplate;
    private JdbcReviewDao jdbcReviewDao;

    public JdbcBreweryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcReviewDao = new JdbcReviewDao(jdbcTemplate);
    }

    @Override
    public List<Brewery> getAllBreweries() {
        List<Brewery> breweries = new ArrayList<>();
        String sql="SELECT * FROM breweries";

        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);

        while(rs.next()){

            Brewery brewery = createBreweryFromRow(rs);
            breweries.add(brewery);
        }

        return breweries;
    }

    @Override
    public Brewery getBreweryByID(long id) {
        Brewery brewery = new Brewery();
        String sql="SELECT * FROM breweries WHERE brewery_id= ?";

        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);

        if(rs.next()) {
            brewery = createBreweryFromRow(rs);
        }
        return brewery;
    }

    @Override
    public boolean createBrewery() {
        return false;
    }

    public Brewery createBreweryFromRow(SqlRowSet rs) {
        Brewery brewery = new Brewery();

        brewery.setId(rs.getLong("brewery_id"));
        brewery.setName(rs.getString("brewery_name"));
        brewery.setEmail(rs.getString("email"));
        brewery.setPhoneNumber(rs.getString("phone"));
        brewery.setIgLink(rs.getString("ig_link"));
        brewery.setFbLink(rs.getString("fb_link"));
        brewery.setAboutUs(rs.getString("about_us"));
        brewery.setAddress(rs.getString("street_address"));
        brewery.setImageURL(rs.getString("img_url"));
        brewery.setGpsLocation(rs.getString("gps_coords"));
        brewery.setFood(rs.getBoolean("food_available"));
        brewery.setOfferings(getBeerByBrewery(brewery.getId()));
        brewery.setReviews(jdbcReviewDao.getBreweryReviews(brewery.getId()));

        return brewery;
    }

    private List<Beer> getBeerByBrewery(long id) {
        List<Beer> offeringList = new ArrayList<>();

        String sql = "SELECT * FROM beers WHERE brewery_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);

        while (rs.next()){
            Beer beer = createBeerFromRow(rs);
            offeringList.add(beer);
        }

        return offeringList;
    }

    private Beer createBeerFromRow(SqlRowSet rs) {
        Beer beer = new Beer();
        beer.setId(rs.getLong("beer_id"));
        beer.setName(rs.getString("beer_name"));
        beer.setDescription(rs.getString("beer_description"));
        beer.setAbv(rs.getDouble("beer_abv"));
        beer.setImageURL(rs.getString("image_url"));
        beer.setStyle(rs.getString("beer_style"));
        beer.setProfile(rs.getString("flavor_profile"));
        beer.setReviews(jdbcReviewDao.getBeerReviews(beer.getId()));

        return beer;
    }


}