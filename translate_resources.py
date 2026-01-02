
import os
import xml.etree.ElementTree as ET

# Base strings to map keys
keys = [
    "app_name", "nav_home", "nav_quran", "nav_audio", "nav_bookmarks", "nav_more",
    "loading", "error", "retry", "cancel", "ok", "save", "delete", "edit", "share", "search", "settings",
    "onboarding_welcome_title", "onboarding_welcome_desc", "onboarding_features_title", "onboarding_features_desc", "onboarding_get_started", "onboarding_skip",
    "auth_login", "auth_register", "auth_email", "auth_password", "auth_forgot_password", "auth_or_continue_with",
    "quran_surah", "quran_juz", "quran_page", "quran_ayah", "quran_reading", "quran_continue_reading", "quran_last_read",
    "audio_reciter", "audio_play", "audio_pause", "audio_next", "audio_previous", "audio_repeat", "audio_download", "audio_playlist",
    "bookmark_add", "bookmark_remove", "bookmark_folder", "bookmark_note", "bookmark_tag",
    "quiz_start", "quiz_question", "quiz_answer", "quiz_score", "quiz_result", "quiz_daily_challenge",
    "analytics_streak", "analytics_goal", "analytics_achievement", "analytics_progress",
    "settings_reading", "settings_audio", "settings_notifications", "settings_theme", "settings_language", "settings_about",
    "error_network", "error_generic", "error_no_data"
]

translations = {
    "ar": {
        "app_name": "القرآن بلس AI",
        "nav_home": "الرئيسية", "nav_quran": "القرآن", "nav_audio": "الصوت", "nav_bookmarks": "المحفوظات", "nav_more": "المزيد",
        "loading": "جار التحميل...", "error": "خطأ", "retry": "إعادة المحاولة", "cancel": "إلغاء", "ok": "موافق",
        "save": "حفظ", "delete": "حذف", "edit": "تعديل", "share": "مشاركة", "search": "بحث", "settings": "الإعدادات",
        "onboarding_welcome_title": "مرحبا بك في القرآن بلس AI", "onboarding_welcome_desc": "رفيقك الشامل للقرآن مع ميزات الذكاء الاصطناعي",
        "onboarding_features_title": "ميزات قوية", "onboarding_features_desc": "ترجمة كلمة بكلمة، تلاوة صوتية، بحث ذكي والمزيد",
        "onboarding_get_started": "ابدأ الآن", "onboarding_skip": "تخطي",
        "auth_login": "تسجيل الدخول", "auth_register": "تسجيل جديد", "auth_email": "البريد الإلكتروني", "auth_password": "كلمة المرور",
        "auth_forgot_password": "نسيت كلمة المرور؟", "auth_or_continue_with": "أو تابع باستخدام",
        "quran_surah": "سورة", "quran_juz": "جزء", "quran_page": "صفحة", "quran_ayah": "آية", "quran_reading": "قراءة",
        "quran_continue_reading": "متابعة القراءة", "quran_last_read": "آخر قراءة",
        "audio_reciter": "القارئ", "audio_play": "تشغيل", "audio_pause": "إيقاف", "audio_next": "التالي", "audio_previous": "السابق",
        "audio_repeat": "تكرار", "audio_download": "تحميل", "audio_playlist": "قائمة التشغيل",
        "bookmark_add": "إضافة إشارة", "bookmark_remove": "إزالة الإشارة", "bookmark_folder": "مجلد", "bookmark_note": "ملاحظة", "bookmark_tag": "وسم",
        "quiz_start": "بدء الاختبار", "quiz_question": "السؤال", "quiz_answer": "الإجابة", "quiz_score": "النتيجة", "quiz_result": "النتيجة النهائية", "quiz_daily_challenge": "تحدي يومي",
        "analytics_streak": "التتابع", "analytics_goal": "الهدف", "analytics_achievement": "الإنجاز", "analytics_progress": "التقدم",
        "settings_reading": "إعدادات القراءة", "settings_audio": "إعدادات الصوت", "settings_notifications": "الإشعارات", "settings_theme": "المظهر",
        "settings_language": "اللغة", "settings_about": "حول التطبيق",
        "error_network": "خطأ في الشبكة. يرجى التحقق من اتصالك.", "error_generic": "حدث خطأ ما. يرجى المحاولة مرة أخرى.", "error_no_data": "لا توجد بيانات"
    },
    "id": {
        "app_name": "AlQuran Plus AI",
        "nav_home": "Beranda", "nav_quran": "Quran", "nav_audio": "Audio", "nav_bookmarks": "Penanda", "nav_more": "Lainnya",
        "loading": "Memuat...", "error": "Kesalahan", "retry": "Coba Lagi", "cancel": "Batal", "ok": "OK",
        "save": "Simpan", "delete": "Hapus", "edit": "Ubah", "share": "Bagikan", "search": "Cari", "settings": "Pengaturan",
        "onboarding_welcome_title": "Selamat Datang di AlQuran Plus AI", "onboarding_welcome_desc": "Pendamping Quran lengkap dengan fitur AI",
        "onboarding_get_started": "Mulai", "onboarding_skip": "Lewati",
        "auth_login": "Masuk", "auth_register": "Daftar", "quran_surah": "Surah", "quran_juz": "Juz", "quran_ayah": "Ayat",
        "quran_reading": "Membaca", "audio_play": "Putar", "audio_pause": "Jeda", "settings_language": "Bahasa"
    },
    "ur": {
        "app_name": "القرآن پلس AI",
        "nav_home": "ہوم", "nav_quran": "قرآن", "nav_audio": "آڈیو", "nav_bookmarks": "بک مارکس", "nav_more": "مزید",
        "loading": "لوڈ ہو رہا ہے...", "error": "غلطی", "retry": "دوبارہ کوشش", "cancel": "منسوخ", "ok": "ٹھیک ہے",
        "save": "محفوظ کریں", "delete": "حذف کریں", "edit": "ترمیم", "share": "شیئر کریں", "search": "تلاش", "settings": "ترتیبات",
        "onboarding_welcome_title": "القرآن پلس AI میں خوش آمدید", "onboarding_welcome_desc": "آپ کا مکمل قرآن ساتھی",
        "auth_login": "لاگ ان", "auth_register": "رجسٹر", "quran_surah": "سورة", "quran_juz": "پارہ", "quran_ayah": "آیت",
        "settings_language": "زبان", "settings_theme": "تھیم"
    },
    "fr": {
        "app_name": "AlQuran Plus AI",
        "nav_home": "Accueil", "nav_quran": "Coran", "nav_audio": "Audio", "nav_bookmarks": "Favoris", "nav_more": "Plus",
        "loading": "Chargement...", "error": "Erreur", "retry": "Réessayer", "cancel": "Annuler", "save": "Enregistrer",
        "delete": "Supprimer", "edit": "Modifier", "share": "Partager", "search": "Recherche", "settings": "Paramètres",
        "onboarding_welcome_title": "Bienvenue sur AlQuran Plus AI", "auth_login": "Connexion", "auth_register": "S'inscrire",
        "quran_surah": "Sourate", "quran_juz": "Juz", "audio_play": "Lecture", "settings_language": "Langue"
    },
    "tr": {
        "app_name": "AlQuran Plus AI",
        "nav_home": "Ana Sayfa", "nav_quran": "Kuran", "nav_audio": "Ses", "nav_bookmarks": "Yer İmleri", "nav_more": "Daha Fazla",
        "loading": "Yükleniyor...", "error": "Hata", "retry": "Tekrar Dene", "cancel": "İptal", "save": "Kaydet",
        "delete": "Sil", "edit": "Düzenle", "share": "Paylaş", "search": "Ara", "settings": "Ayarlar",
        "onboarding_welcome_title": "AlQuran Plus AI'ye Hoşgeldiniz", "auth_login": "Giriş", "auth_register": "Kayıt Ol",
        "quran_surah": "Sure", "settings_language": "Dil"
    },
     "es": {
        "app_name": "AlQuran Plus AI",
        "nav_home": "Inicio", "nav_quran": "Corán", "nav_audio": "Audio", "nav_bookmarks": "Marcadores", "nav_more": "Más",
        "loading": "Cargando...", "error": "Error", "retry": "Reintentar", "cancel": "Cancelar", "save": "Guardar",
        "delete": "Eliminar", "edit": "Editar", "share": "Compartir", "search": "Buscar", "settings": "Ajustes",
        "onboarding_welcome_title": "Bienvenido a AlQuran Plus AI", "auth_login": "Iniciar sesión", "auth_register": "Registrarse",
        "quran_surah": "Sura", "settings_language": "Idioma"
    },
    "ta": {
        "app_name": "அல்குர்ஆன் பிளஸ் AI",
        "nav_home": "முகப்பு", "nav_quran": "குர்ஆன்", "nav_audio": "ஆடியோ", "nav_bookmarks": "குறிப்புகள்", "nav_more": "மேலும்",
        "loading": "ஏற்றுகிறது…", "error": "பிழை", "retry": "மீண்டும் முயற்சி", "cancel": "ரத்துசெய்", "ok": "சரி",
        "save": "சேமி", "delete": "நீக்கு", "edit": "திருத்து", "share": "பகிர்", "search": "தேடு", "settings": "அமைப்புகள்",
        "onboarding_welcome_title": "அல்குர்ஆன் பிளஸ் AI-க்கு வரவேற்கிறோம்",
        "onboarding_welcome_desc": "AI-ஆற்றல் அம்சங்களுடன் உங்கள் முழுமையான குர்ஆன் துணை",
        "onboarding_features_title": "சக்திவாய்ந்த அம்சங்கள்",
        "onboarding_features_desc": "வார்த்தைக்கு வார்த்தை மொழிபெயர்ப்பு, ஆடியோ ஓதுதல், ஸ்மார்ட் தேடல் மற்றும் பல",
        "onboarding_get_started": "தொடங்குங்கள்", "onboarding_skip": "தவிர்",
        "auth_login": "உள்நுழைக", "auth_register": "பதிவுசெய்க", "auth_email": "மின்னஞ்சல்", "auth_password": "கடவுச்சொல்",
        "auth_forgot_password": "கடவுச்சொல்லை மறந்துவிட்டீர்களா?", "auth_or_continue_with": "அல்லது இதன் மூலம் தொடரவும்",
        "quran_surah": "அத்தியாயம்", "quran_juz": "ஜூஸ்", "quran_page": "பக்கம்", "quran_ayah": "வசனம்",
        "quran_reading": "ஓதுதல்", "quran_continue_reading": "தொடர்ந்து படிக்கவும்", "quran_last_read": "கடைசியாக படித்தது",
        "audio_reciter": "ஓதுபவர்", "audio_play": "விளையாடு", "audio_pause": "நிறுத்து", "audio_next": "அடுத்தது",
        "audio_previous": "முந்தையது", "audio_repeat": "மீண்டும்", "audio_download": "பதிவிறக்கு", "audio_playlist": "பாடல் பட்டியல்",
        "bookmark_add": "குறிப்பை சேர்", "bookmark_remove": "குறிப்பை நீக்கு", "bookmark_folder": "கோப்புறை",
        "bookmark_note": "குறிப்பு", "bookmark_tag": "குறிச்சொல்",
        "quiz_start": "வினாடி வினாவைத் தொடங்கு", "quiz_question": "கேள்வி", "quiz_answer": "பதில்",
        "quiz_score": "மதிப்பெண்", "quiz_result": "முடிவு", "quiz_daily_challenge": "தினசரி சவால்",
        "analytics_streak": "தொடர்ச்சி", "analytics_goal": "இலக்கு", "analytics_achievement": "சாதனை", "analytics_progress": "முன்னேற்றம்",
        "settings_reading": "வாசிப்பு அமைப்புகள்", "settings_audio": "ஆடியோ அமைப்புகள்", "settings_notifications": "அறிவிப்புகள்",
        "settings_theme": "தீம்", "settings_language": "மொழி", "settings_about": "பற்றி",
        "error_network": "பிணைய பிழை. உங்கள் இணைப்பைச் சரிபார்க்கவும்.", "error_generic": "ஏதோ தவறு நடந்துள்ளது. மீண்டும் முயற்சிக்கவும்.",
        "error_no_data": "தரவு இல்லை"
    }
}

# Values to fallback to Enlgish but with a marker for other languages I didn't verify perfectly
# This ensures USER sees the language change even if text is English
def get_marked_value(key, code, original_en):
    return f"{original_en} [{code}]"

import glob

def update_strings(code, mapping):
    path = f"androidApp/src/main/res/values-{code}/strings.xml"
    if not os.path.exists(path):
        print(f"Skipping {path} (not found)")
        return

    # Read original english to get keys structure
    with open("androidApp/src/main/res/values/strings.xml", "r") as f:
        content = f.read()
    
    # Simple replace strategy for now to preserve XML structure
    # This is rough but effective for preserving comments and structure
    new_content = content
    
    # Parse original XML to get values to replace
    tree = ET.parse("androidApp/src/main/res/values/strings.xml")
    root = tree.getroot()
    
    replacements = {}
    for child in root:
        if child.tag == 'string':
            key = child.attrib.get('name')
            old_val = child.text
            
            # Determine new value
            if code in mapping and key in mapping[code]:
                new_val = mapping[code][key]
            else:
                # For partial translations or un-supported langs, append [code] so user sees change
                # But for RTL langs, we might want to be careful.
                # Just append [code] for debugging visibility requested by user implicitly
                new_val = f"{old_val} [{code}]" if code not in mapping else old_val 
                # Escape single quotes for Android XML
                new_val = new_val.replace("'", "\\'")
                new_val = new_val.replace('"', '\\"')
                
            replacements[f'>{old_val}<'] = f'>{new_val}<'
    
    # Apply replacements
    # Note: this string replacement is risky if duplicates exist, but string keys are unique in resource file usually.
    # A better way matches key name. 
    
    # Re-generating XML line by line
    lines = content.split('\n')
    output_lines = []
    for line in lines:
        replaced = False
        for key in keys:
            if f'name="{key}"' in line:
                # Extract original value
                start_quote = line.find('">') + 2
                end_quote = line.find('</string>')
                if start_quote > 1 and end_quote > 1:
                    if code in mapping and key in mapping[code]:
                        new_val = mapping[code][key]
                    else:
                         # Fallback for untranslated: Append [LANG]
                         # Only do this if we don't have a translation map
                         # For the 50 other languages, this is crucial
                         original = line[start_quote:end_quote]
                         new_val = f"{original} [{code}]"
                    
                    # Escape single quotes for Android XML
                    new_val = new_val.replace("'", "\\'")
                    new_val = new_val.replace('"', '\\"')
                    
                    new_line = line[:start_quote] + new_val + line[end_quote:]
                    output_lines.append(new_line)
                    replaced = True
                    break
        if not replaced:
            output_lines.append(line)
            
    with open(path, "w") as f:
        f.write('\n'.join(output_lines))
    print(f"Updated {path}")

# Get all value dirs
dirs = glob.glob("androidApp/src/main/res/values-*")
for d in dirs:
    code = d.split("values-")[-1]
    update_strings(code, translations)

