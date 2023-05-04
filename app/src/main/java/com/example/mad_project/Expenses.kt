import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.auth.FirebaseAuth

class Expenses(var name: String, var amount: Double, var date: Timestamp? = null, var id: String? = "",var userid: String?) {

    constructor() : this("", 0.0, null, null,null)

    fun getDateString(): String {
        if (date == null) {
            return ""
        }
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return sdf.format(date!!.toDate())
    }
}
