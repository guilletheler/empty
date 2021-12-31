package com.gt.vacio.web.bean;

import java.io.Serializable;
import java.util.Date;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.gt.toolbox.spb.webapps.commons.infra.utils.Utils;

@Named
@ViewScoped
public class TableExporterUtilsMB implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void acomodaXls(Object document) {

		Workbook wb = (Workbook) document;
		Sheet sheet = wb.getSheetAt(0);

		DataFormatter formatter = new DataFormatter();
		
		CellStyle numberCellStyle = wb.createCellStyle();
		numberCellStyle.setAlignment(HorizontalAlignment.RIGHT);

		for (Row row : sheet) {
			for (Cell cell : row) {
				String strValue = formatter.formatCellValue(cell);

				strValue = strValue.replace(",", ".");
				strValue = strValue.replace("'", "");

				if (NumberUtils.isCreatable(strValue)) {
					cell.setCellValue(Double.parseDouble(strValue));
					cell.setCellStyle(numberCellStyle);
				} else {
					strValue = formatter.formatCellValue(cell);
				}
			}
		}
	}

	public String fechaActual() {
		return Utils.SDF_DMYY.format(new Date());
	}

	public String fechaExporta() {
		return Utils.SDF_DMYY.format(new Date());
	}
}
