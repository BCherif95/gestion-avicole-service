package com.gestvicole.gestionavicole.repositories;

import com.gestvicole.gestionavicole.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Invoice findInvoiceById(@Param("id") Long id);

    @Query("SELECT i from Invoice i where DATE(i.invoiceDate) = DATE(:invoiceDate)")
    List<Invoice> findAllInvoiceByDate(@Param("invoiceDate") Date invoiceDate);

    @Query("SELECT sum(i.amount) from Invoice i where DATE_FORMAT(i.invoiceDate, '%Y%m') = DATE_FORMAT(:invoiceDate, '%Y%m')")
    Integer sumTotalByInvoiceDate(@Param("invoiceDate") Date invoiceDate);

    @Query("SELECT sum(i.amountPaid) from Invoice i where DATE(i.invoiceDate) = DATE(:date)")
    Integer sumTotalAmountPaidByDate(@Param("date") Date date);

    @Query("SELECT sum(i.stayToPay) from Invoice i where DATE(i.invoiceDate) = DATE(:date)")
    Integer sumNetStayToPayByDate(@Param("date") Date date);
}
