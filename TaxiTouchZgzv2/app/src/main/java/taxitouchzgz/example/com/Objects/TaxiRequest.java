package taxitouchzgz.example.com.Objects;

import java.util.ArrayList;

/**
 * Created by sergiolazaromagdalena on 11/7/15.
 */
public class TaxiRequest {


        private Integer count;
        private ArrayList<TaxiStop> rows = new ArrayList<TaxiStop>();

        public TaxiRequest(){}

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public ArrayList<TaxiStop> getRows() {
            return rows;
        }

        public void setRows(ArrayList<TaxiStop> rows) {
            this.rows = rows;
        }

}
