package com.cdek.courier.ui.base.map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import java.util.HashMap;

public class TwoGisHelper {
    private HashMap<String, String> cityMap;

    public void onCreate() {
        if (cityMap == null)
            createMap();
    }

    private void createMap() {
        cityMap = new HashMap<String, String>();
        cityMap.put("823", "abakan");
        cityMap.put("4756", "almaty");
        cityMap.put("315", "armawir");
        cityMap.put("402", "arkhangelsk");
        cityMap.put("432", "astrakhan");

        cityMap.put("274", "barnaul");
        cityMap.put("337", "belgorod");
        cityMap.put("275", "biysk");
        cityMap.put("286", "blagoveshensk");
        cityMap.put("877", "bratsk");
        cityMap.put("220", "bryansk");

        cityMap.put("139", "v_novgorod");
        cityMap.put("288", "vladivostok");
        cityMap.put("94", "vladimir");
        cityMap.put("426", "volgograd");
        cityMap.put("427", "volgograd");
        cityMap.put("246", "vologda");
        cityMap.put("506", "voronezh");

        cityMap.put("250", "ekaterinburg");
        cityMap.put("164", "ivanovo");
        cityMap.put("224", "izhevsk");
        cityMap.put("281", "irkutsk");
        cityMap.put("141", "yoshkarola");

        cityMap.put("424", "kazan");
        cityMap.put("152", "kaliningrad");
        cityMap.put("142", "kaluga");
        cityMap.put("272", "kemerovo");
        cityMap.put("415", "kirov");
        cityMap.put("903", "komsomolsk");
        cityMap.put("165", "kostroma");
        cityMap.put("435", "krasnodar");
        cityMap.put("278", "krasnoyarsk");
        cityMap.put("93", "kurgan");
        cityMap.put("699", "kursk");

        cityMap.put("320", "lipetsk");
        cityMap.put("258", "magnitogorsk");
        cityMap.put("44", "moscow");
        cityMap.put("521", "moscow");
        cityMap.put("184", "moscow");
        cityMap.put("57", "moscow");
        cityMap.put("920", "moscow");
        cityMap.put("520", "moscow");
        cityMap.put("157", "moscow");
        cityMap.put("45", "moscow");
        cityMap.put("168", "moscow");


        cityMap.put("433", "nabchelny");
        cityMap.put("501", "nahodka");
        cityMap.put("255", "nizhnevartovsk");
        cityMap.put("414", "n_novgorod");
        cityMap.put("273", "novokuznetsk");
        cityMap.put("773", "novokuznetsk");
        cityMap.put("436", "novorossiysk");
        cityMap.put("270", "novosibirsk");

        cityMap.put("268", "omsk");
        cityMap.put("261", "orenburg");
        cityMap.put("169", "orel");

        cityMap.put("504", "penza");
        cityMap.put("248", "perm");
        cityMap.put("450", "petrozavodsk");
        cityMap.put("393", "pskov");
        cityMap.put("151", "minvody");
        cityMap.put("199", "minvody");
        cityMap.put("1085", "minvody");

        cityMap.put("438", "rostov");
        cityMap.put("1069", "rostov");
        cityMap.put("159", "ryazan");
        cityMap.put("430", "samara");
        cityMap.put("137", "spb");
        cityMap.put("417", "saransk");
        cityMap.put("428", "saratov");
        cityMap.put("395", "smolensk");
        cityMap.put("437", "sochi");
        cityMap.put("439", "stavropol");
        cityMap.put("132", "staroskol");
        cityMap.put("257", "sterlitamak");
        cityMap.put("254", "surgut");
        cityMap.put("404", "syktyvkar");

        cityMap.put("298", "tambov");
        cityMap.put("245", "tver");

        cityMap.put("431", "togliatti");
        cityMap.put("269", "tomsk");
        cityMap.put("150", "tula");
        cityMap.put("252", "tyumen");

        cityMap.put("230", "ulanude");
        cityMap.put("422", "ulyanovsk");
        cityMap.put("955", "ussuriysk");
        cityMap.put("11903", "ustkam");
        cityMap.put("256", "ufa");

        cityMap.put("287", "khabarovsk");
        cityMap.put("419", "cheboksary");
        cityMap.put("259", "chelyabinsk");
        cityMap.put("231", "chita");
        cityMap.put("473", "yuzhnosakhalinsk");
        cityMap.put("283", "yakutsk");
        cityMap.put("146", "yaroslavl");
    }

    public String getCityUrlName(String cityId) {
        if (cityMap == null)
            createMap();

        return cityMap.get(cityId);
    }

    public void handle2Gis(Activity activity, String cityId, CharSequence search) {
        String url = "http://2gis.ru/";
        String cityPath = getCityUrlName(cityId);
        if (cityPath != null)
            if (!cityPath.isEmpty())
                url += cityPath + "/";

        url += "search/" + search;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        activity.startActivity(i);
    }
}
