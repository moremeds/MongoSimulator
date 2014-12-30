package mongo.rec;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

class RecordGenerator {

    private static final int ID_LENGTH = 17;

    private static final String[] ATTESTATION_OPTIONS = {"Yes, I received the following business gifts/entertainment in 2014/Q.", "No, I did not receive anything."};

    private static final float COST_RANGE_START = 0F;
    private static final float COST_RANGE_END = 200F;

    private static final String DATE_START = "2014-06-01 00:00:00.000";
    private static final String DATE_END = "2014-12-31 00:00:00.000";

    private static final String[] DESCRIPTION_OPTIONS = {"Dinner/Drinks", "Ticket"};

    private static final String[] MODE_OPTIONS = {"Entertainment", "Gift"};

    private static final String[] RECEIVE_OPTIONS = {"Acme Corp Ltd.", "Google", "MicroSoft", "J.P.Morgan", "Morgan Stanley", "Goldman Sachs", "HSBC"};

    private ArrayList<User> users;

    RecordGenerator() {
        this.users = new ArrayList<User>();
        users.add(new User("fTSwakgbXb63dCHyQ", "Jane Doe"));
        users.add(new User("tzF3K4vZtABHdvpPo", "John Doe"));
        users.add(new User("bLY5yQXwqNgE8y5i4", "John Smith"));
        users.add(new User("6G7EEePu2oGK7Kuyi", "Bill Gates"));
        users.add(new User("CN3RsascTgyTjsEma", "Tony Stark"));
    }

//	public String genRecordId() {
//		return this.genRandomString(RecordGenerator.ID_LENGTH);
//	}

    public String genAttestation() {
        return this.getRandEleFromArray(ATTESTATION_OPTIONS);
    }

    public double genCost() {
        double cost = COST_RANGE_START + Math.random()
                * Math.abs(COST_RANGE_END - COST_RANGE_START);
        return Double.parseDouble(String.format("%.2f", cost));
    }


    public long genDate(String startDateStr, String endDateStr) {

        long beginTime = Timestamp.valueOf(startDateStr).getTime();
        long endTime = Timestamp.valueOf(endDateStr).getTime();
        long diff = endTime - beginTime + 1;
        long dateLong = beginTime + (long) (Math.random() * diff);


        return dateLong;
    }

    public String dateToStr(long dateLong, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date randomDate = new Date(dateLong);
        return dateFormat.format(randomDate);
    }

    public String genDescription() {
        return this.getRandEleFromArray(DESCRIPTION_OPTIONS);
    }

    public String genMode() {
        return this.getRandEleFromArray(MODE_OPTIONS);
    }

    public String genReceive() {
        return this.getRandEleFromArray(RECEIVE_OPTIONS);
    }


    // / helpers

    private int getRandIndexOfArray(String[] arr) {
        int randIndex = (int) (Math.floor(Math.random() * arr.length));
        return randIndex;
    }

    private int getRandIndexOfArray(int[] arr) {
        int randIndex = (int) (Math.floor(Math.random() * arr.length));
        return randIndex;
    }

    private String getRandEleFromArray(String[] arr) {
        return arr[this.getRandIndexOfArray(arr)];
    }

    private int getRandEleFromArray(int[] arr) {
        return arr[this.getRandIndexOfArray(arr)];
    }

    public String genRandomString(int length) {
        // length should >0
        if (length < 0) {
            throw new IllegalArgumentException();
        }
        String strSource = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        // string buffer for optional chars.
        StringBuffer stringBuffer = new StringBuffer(strSource);
        // string buffer for the result random string.
        StringBuffer objStringBuffer = new StringBuffer();

        Random r = new Random();

        int range = stringBuffer.length();
        for (int i = 0; i < length; i++) {
            objStringBuffer.append(stringBuffer.charAt(r.nextInt(range)));
        }
        return objStringBuffer.toString();
    }

    // /

    public String genRandomRecord() {

        JSONObject record = new JSONObject();

        // date
        long date = this.genDate(DATE_START, DATE_END);
        String dateStr = this.dateToStr(date, "yyyy-MM-dd");
        record.put("date", dateStr);

        // submit date
        String dateF = this.dateToStr(date, "yyyy-MM-dd HH:mm:ss.SSS");
        long submitDate = this.genDate(dateF, DATE_END);
        String submitDateStr = this.dateToStr(submitDate, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        record.put("submitted",
                new JSONObject().put("$date", submitDateStr));

        // name, author, id
        int randIndex = (int) (Math.floor(Math.random() * users.size()));
        record.put("name", users.get(randIndex).getUsername());
        record.put("author", users.get(randIndex).getUsername());
        record.put("userId", users.get(randIndex).getId());

        // attestation
        String att = this.genAttestation();
        record.put("attestation", att);

        if (att.equalsIgnoreCase(ATTESTATION_OPTIONS[0])) {
            // has values
            record.put("cost", this.genCost());
            record.put("description", this.genDescription());
            record.put("mode", this.genMode());
            record.put("receive", this.genReceive());
        } else {
            // no value
            record.put("cost", "");
            record.put("description", "");
            record.put("mode", "");
            record.put("receive", "");
        }


        String jsonText = record.toString(2);

        return jsonText;
    }


}