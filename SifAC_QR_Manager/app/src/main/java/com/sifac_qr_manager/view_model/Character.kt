//package com.sifac_qr_manager.view_model
//
//import android.content.Context
//import com.sifac_qr_manager.R
//
//enum class Group {
//    Muses,
//    Aqours
//}
//
//enum class Character {
//    Honoka,
//    Eli,
//    Kotori,
//    Umi,
//    Rin,
//    Maki,
//    Nozomi,
//    Hanayo,
//    Niko,
//    Chika,
//    Riko,
//    Kanan,
//    Dia,
//    You,
//    Yoshiko,
//    Hanamaru,
//    Mary,
//    Ruby
//}
//
//class GroupEnumConverter {
//    companion object {
//        fun ToString(context: Context, group: Group) : String {
//            val array = context.resources.getStringArray(R.array.group_array)
//            return array[group.ordinal]
//        }
//
//        fun ToEnum(context: Context, group: String) : Group {
//            val array = context.resources.getStringArray(R.array.group_array)
//            return Group.values()[array.indexOf(group)]
//        }
//    }
//}
//
//class CharacterEnumConverter {
//    companion object {
//        fun ToString(context: Context, character: Character) : String {
//            val array = context.resources.getStringArray(R.array.character_array)
//            return array[character.ordinal]
//        }
//
//        fun ToEnum(context: Context, character: String) : Character {
//            val array = context.resources.getStringArray(R.array.character_array)
//            return Character.values()[array.indexOf(character)]
//        }
//    }
//}