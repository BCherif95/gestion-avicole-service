package com.gestvicole.gestionavicole.repositories;

import com.gestvicole.gestionavicole.entities.Payment;
import com.gestvicole.gestionavicole.utils.Enumeration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findAllByInvoiceId(Long invoice_id);

    Payment findPaymentById(@Param(value = "id") Long id);

    List<Payment> findAllByCanceled(boolean canceled);

    List<Payment> findAllByIdLessThanAndInvoiceIdAndCanceledAndStateEquals(@Param("id") Long id,
                                                                                  @Param("invoiceId") Long invoiceId,
                                                                                  @Param("canceled") boolean canceled,
                                                                                  @Param("state") Enumeration.PAYMENT_STATE state);

    List<Payment> findAllByIdGreaterThanAndInvoiceIdAndCanceledAndStateEquals(@Param("id") Long id,
                                                                                     @Param("invoiceId") Long invoiceId,
                                                                                     @Param("canceled") boolean canceled,
                                                                                     @Param("state") Enumeration.PAYMENT_STATE state);
}
