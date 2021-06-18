/*
 * Copyright (c) 2021. ArseneLupin0.
 *
 * Licensed under the GNU General Public License v3.0
 *
 * https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Permissions of this strong copyleft license are conditioned on making available complete source
 * code of licensed works and modifications, which include larger works using a licensed work,
 * under the same license. Copyright and license notices must be preserved. Contributors provide
 * an express grant of patent rights.
 */

package es.miapp.ad.ej1saincahi.model.pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class Call {
    private Date date;
    private String number, name;
    private int times;

    public static Call CSVToStringDate(String csv, String separator) {
        Call call = null;
        String[] partes = csv.split(separator);

        if (partes.length == 9) {
            String rightDate = partes[0] + "; " + partes[1] + "; " + partes[2] + "; " + partes[3] + "; " + partes[4] + "; " + partes[5];
            Date date = getCorrectDate(rightDate);
            call = new Call(date, partes[6].trim(), partes[7].trim(), Integer.parseInt(partes[8].trim()));
        }
        return call;
    }

    private static Date getCorrectDate(String parseDate) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy; MM; dd; HH; mm; ss", Locale.getDefault()).parse(parseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String toCSV() {
        return new SimpleDateFormat("yyyy; MM; dd; HH; mm; ss", Locale.getDefault()).format(date) + "; " + number + "; " + name + "; " + times;
    }

    public String toCSVv2() {
        return name + "; " + times + "; " + new SimpleDateFormat("yyyy; MM; dd; HH; mm; ss", Locale.getDefault()).format(date) + "; " + number;
    }

    public int compareFecha(Call c) {
        return date.compareTo(c.date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Call call = (Call) o;
        return number.equals(call.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}