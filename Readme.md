# Applicazione Radio per Android su board Nomadik.

**ATTENZIONE**: C'è un po' di setup da fare per poter usare certe parti del progetto. In particolare:

	sh jni/bootstrap.sh <percorso anche relativo alla root dell'Android NDK con cui compilare>
	
Questo è sufficiente per effettuare la build della componente JNI con il metodo standard dell'NDK (`make APP=NomadikRadio`) e con il progetto Xcode in `jni/libNomadikRadio`. Per usare i due file `.launch` per Eclipse, è necessario impostare la variabile `android.ndk` con il percorso assoluto alla root dell'NDK nelle preferenze di Eclipse sotto **Run/Debug** > **String Substitution**.

In futuro probabilmente `build.xml` avrà un target apposito per la libreria JNI. Fino ad allora…
