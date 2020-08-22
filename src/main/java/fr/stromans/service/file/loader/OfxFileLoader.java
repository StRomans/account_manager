package fr.stromans.service.file.loader;

import fr.stromans.domain.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class OfxFileLoader implements IFileLoader {

    private static final String TRANSACTION_TAG_STARTER = "<STMTTRN>";
    private static final String TRANSACTION_TAG_ENDER = "</STMTTRN>";

    private static final String TRANSACTION_TAG_DATE = "<DTPOSTED>";
    private static final String TRANSACTION_TAG_AMOUNT = "<TRNAMT>";
    private static final String TRANSACTION_TAG_LABEL = "<NAME>";
    private static final String TRANSACTION_TAG_ID = "<FITID>";

    private static final String FILE_TAG_STARTDATE = "<DTSTART>";
    private static final String FILE_TAG_ENDDATE = "<DTEND>";

    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> content;
    private DateTimeFormatter dateFormatter;

    private OfxFileLoader(List<String> lines){

        List<String> normalizedLines = new LinkedList<>();
        for (String line: lines) {
            normalizedLines.add(line.replaceAll("<", "\n<"));
        }
        this.setContent(normalizedLines);
        this.setDateFormatter(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public List<Transaction> parse(){

        LinkedList<Transaction> foundTransactions = new LinkedList<>();

        Transaction transaction = null;
        for(String line : this.getContent()){

            // Ne pas parser l'entête
            if(line.contains("<") && line.contains(">")){
                // Extraire le tag via regex
                String tag = line.substring(line.indexOf("<"),line.indexOf(">") +1);
                String value = line.replaceFirst(tag, "");

                switch (tag) {

                    case FILE_TAG_STARTDATE:
                        this.setStartDate(this.getDate(value));
                        break;

                    case FILE_TAG_ENDDATE:
                        this.setEndDate(this.getDate(value));
                        break;

                    case TRANSACTION_TAG_STARTER:
                        transaction = new Transaction();
                        break;

                    case TRANSACTION_TAG_DATE:
                        transaction.setDate(this.getDate(value));
                        break;

                    case TRANSACTION_TAG_AMOUNT:
                        // pour remplacer 1000,00 par 1000.00
                        String safeValue = value.replaceAll(",", ".");
                        // pour remplacer 1 000.00 par 1000.00
                        safeValue = safeValue.replaceAll(" ", "");
                        transaction.setAmount(BigDecimal.valueOf(Double.parseDouble(safeValue)));
                        break;

                    case TRANSACTION_TAG_LABEL:
                        transaction.setLabel(value);
                        break;

                    case TRANSACTION_TAG_ID :
                        transaction.setIdentifier(value);
                        break;

                    case TRANSACTION_TAG_ENDER:
                        foundTransactions.add(transaction);
                        break;

                    default:
                        break;
                }
            }
        }

        return foundTransactions;
    }

    /**
     * @return the content
     */
    public List<String> getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(List<String> content) {
        this.content = content;
    }

    /**
     * @param simpleDateFormat
     */
    private void setDateFormatter(DateTimeFormatter simpleDateFormat) {
        this.dateFormatter = simpleDateFormat;
    }

    /**
     * @return the content
     */
    private LocalDate getDate(String strDate) {
        return LocalDate.parse(strDate, this.dateFormatter);
    }

    /**
     * @return the startDate
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
