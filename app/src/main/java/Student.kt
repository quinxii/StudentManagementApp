import android.os.Parcel
import android.os.Parcelable
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey
import io.realm.kotlin.types.ObjectId
import java.util.Objects

class Student() : RealmObject, Parcelable {
    @PrimaryKey
    var id: ObjectId = ObjectId.create()
    var stuname: String = ""
    var stubirth: String = ""
    var stuclasses: String = ""
    var stugender: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()?.let {ObjectId.from(it)}!!
        stuname = parcel.readString().toString()
        stubirth = parcel.readString().toString()
        stuclasses = parcel.readString().toString()
        stugender = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id.toString())
        parcel.writeString(stuname)
        parcel.writeString(stubirth)
        parcel.writeString(stuclasses)
        parcel.writeString(stugender)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        var result: String ="$id - $stuname - $stubirth - $stuclasses - $stugender"
        return result
    }

    companion object CREATOR : Parcelable.Creator<Student> {
        override fun createFromParcel(parcel: Parcel): Student {
            return Student(parcel)
        }

        override fun newArray(size: Int): Array<Student?> {
            return arrayOfNulls(size)
        }
    }
}


