package id.ramenku.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Pesan khusus yang ditampilkan bila data tidak dapat dihapus karena masih dipakai data lain. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PesanBilaTerpakai {
    String value();
}
