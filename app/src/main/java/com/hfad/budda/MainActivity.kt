package com.hfad.budda

import android.app.*
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.hfad.budda.databinding.ActivityMainBinding
import com.hfad.budda.ui.viewmodels.QuoteViewModel
import com.hfad.budda.ui.viewmodels.QuoteViewModelFactory


class MainActivity : AppCompatActivity() {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "1"
    }

    private val viewModel: QuoteViewModel by viewModels {
        QuoteViewModelFactory(
            (application as BuddhaApplication).database.quotesDao(),
            (application as BuddhaApplication).database.settingsDao()
        )
    }

    private lateinit var binding: ActivityMainBinding

    private inline fun <reified T> addFragment() where T : Fragment{
        for (fragment in supportFragmentManager.fragments) {
            supportFragmentManager.beginTransaction().remove(fragment).commit()
        }
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<T>(R.id.fragment)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getInterval() ?: run {
            viewModel.addSettings(1,"12:00","20:00",
                newNotify = true, newTheme = false)
        }
        if (viewModel.getTheme() == true)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigation.selectedItemId = R.id.settings_item
        addFragment<SettingsFragment>()
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.settings_item -> {
                    addFragment<SettingsFragment>()
                    true
                }
                R.id.quotes_item -> {
                    addFragment<QuoteFragment>()
                    true
                }
                else -> false
            }
        }
        binding.addPointButton.setOnClickListener {

            val builder: AlertDialog.Builder = this.let { AlertDialog.Builder(it) }
            builder.setTitle("New quote")
            val input = EditText(this)
            input.hint = "Enter new quote "
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)
            builder.setPositiveButton("OK") { _, _ ->
                viewModel.addQuote(input.text.toString())
            }
            builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            builder.show()
        }

        val num = viewModel.numOfQuotes()
        if(num == 0) {
            val quotes = "Боль неизбежна. Но страдание — личный выбор каждого.\n" +
                    "Будьте мягки с юными, сострадательны с пожилыми, терпимы к слабым и заблуждающимся. Когда-то в своей жизни вы будете или были каждым из них.\n" +
                    "Ваше предназначение в жизни — найти свое предназначение и посвятить ему все свое сердце и душу.\n" +
                    "Ваши страдания вызваны вашим сопротивлением тому, что есть.\n" +
                    "Весь секрет существования заключается в избавлении от страхов. Не бойся того, что с тобой будет, твое будущее от этого не изменится, зато настоящее станет спокойным.\n" +
                    "Всякая усиленная привязанность ко всему земному – страдание.\n" +
                    "Гармония приходит изнутри. Не ищите снаружи то, что может быть только в вашем сердце. Правда в том, что гармонию можно найти только внутри себя.\n" +
                    "Глупец, который знает свою глупость, тем самым уже мудр, а глупец, мнящий себя мудрым, воистину глупец.\n" +
                    "Давай, даже если у тебя мало.\n" +
                    "Даже разумный человек будет глупеть, если он не будет самосовершенствоваться.\n" +
                    "Если вы найдете мудрого критика, который укажет на ваши недостатки, следуйте за ним, как если бы вы нашли скрытое сокровище.\n" +
                    "Если рука не ранена, можно нести яд в руке. Яд не повредит не имеющего ран. Кто сам не делает зла, не подвержен злу.\n" +
                    "Если что-то стоит делать, делайте это всем своим сердцем.\n" +
                    "Есть три вещи, которые невозможно спрятать: солнце, луна и правда.\n" +
                    "Каждое утро мы рождаемся вновь. Только то, что вы делаете сейчас, имеет настоящее значение.\n" +
                    "Когда вы живете во тьме, почему вы не ищете свет?\n" +
                    "Корень страдания – это привязанность.\n" +
                    "Легко увидеть грехи других, свои же, напротив, увидеть трудно. Ибо чужие грехи рассеивают, как шелуху; свои же, напротив, скрывают, как искусный шулер несчастливую кость.\n" +
                    "Медитация приносит мудрость; отсутствие медитации оставляет невежество. Хорошо знай, что ведет тебя вперед, а что задерживает тебя, и выбирай путь, ведущий к мудрости.\n" +
                    "На свете не существует пожара более сильного, чем страсть, акулы более свирепой, чем ненависть, и урагана более опустошительного, чем жадность.\n" +
                    "Наблюдая за собой, вы наблюдаете за другими. Наблюдая за другими, вы наблюдаете за собой.\n" +
                    "Наша жизнь формируется нашими мыслями; мы становимся тем, о чем думаем.\n" +
                    "Не обращайте внимания на то, что делают другие или не делают; обращайте внимание на то, что делаете или не делаете вы.\n" +
                    "Не отвечайте злом на зло, иначе злу не будет конца. В ответ на обиду поцелуй врага своего, и ему станет намного больнее.\n" +
                    "Не пытайтесь строить свое счастье на несчастье других. Иначе вы утонете в ненависти.\n" +
                    "Ненавистью не одолеть ненависть. Лишь любовью ненависть побеждается. Это вечный закон.\n" +
                    "Нет страха для человека, чей ум не исполнен желаний.\n" +
                    "Один из самых полезных жизненных навыков — это умение быстро забывать все плохое: не зацикливаться на неприятностях, не жить обидами, не упиваться раздражением, не таить злобу… Не стоит тащить разный хлам в свою душу.\n" +
                    "Откажитесь от гнева, откажитесь от гордости и освободите себя от мирского рабства. Никакая печаль не может постичь тех, кто никогда не пытается обладать людьми и вещами как своими собственными.\n" +
                    "Побеждай гнев при помощи спокойствия, побеждай зло при помощи добра. Побеждай бедность при помощи щедрости, побеждай ложь при помощи истины.\n" +
                    "Секрет здоровья для ума и тела заключается в том, чтобы не сокрушаться по прошлому, не слишком беспокоиться о будущем, но жить настоящим моментом мудро и искренне.\n" +
                    "Сколько бы мудрых слов ты ни прочел, сколько бы ни произнес, какой тебе от них толк, коль ты не применяешь их на деле?\n" +
                    "Счастье — это не удачное сочетание внешних обстоятельств. Это просто состояние вашего ума.\n" +
                    "Счастье никогда не придет к тому, кто не ценит того, что уже имеет.\n" +
                    "Тот, кто побеждает себя, сильнее того, кто побеждает тысячу раз тысячу человек на поле битвы.\n" +
                    "Ты будешь наказан не за свой гнев; ты будешь наказан своим гневом.\n" +
                    "Ты теряешь только то, за что держишься.\n" +
                    "Человек должен учиться тайнам жизни у самого себя, а не слепо верить в другие учения.\n" +
                    "Что толку в красноречии человека, если он не следует своим словам?"
            val splitQuotes = quotes.split("\n")
            for (quote in splitQuotes) {
                viewModel.addQuote(quote)
            }
        }
    }

    private fun setAlarm(triggerTime: Long, code: Int){

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val ids = viewModel.getQuoteIds()
        val intent = Intent(this, NotifyAlarm::class.java)
        if (ids.isNotEmpty())
            intent.putExtra("quote", viewModel.getQuote(ids.random()))
        else
            intent.putExtra("quote","No any quote")
        val pendingIntent = PendingIntent.getBroadcast(
            this, code, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            AlarmManager.INTERVAL_DAY,
            pendingIntent)
       // Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show()
    }

    private fun cancelAlarm() {
        for (i in 1..viewModel.getInterval()!!) {
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                i,
                Intent(this, NotifyAlarm::class.java),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
            )
            pendingIntent?.cancel()
            //Toast.makeText(this, "Alarm is stopped", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        cancelAlarm()
    }

    override fun onStop() {
        super.onStop()
        Log.d("nott","stopped")
        if (viewModel.getNotify() == true) {

            val firstTimeStrings = viewModel.getFirstTime().split(":")
            val secondTimeStrings = viewModel.getSecondTime().split(":")
            val firstTime =
                (firstTimeStrings[0].toInt() * 60 + firstTimeStrings[1].toInt()) * 60000
            val secondTime =
                (secondTimeStrings[0].toInt() * 60 + secondTimeStrings[1].toInt()) * 60000
            val countDownInterval: Long =
                if(firstTime < secondTime)
                    ((secondTime - firstTime) / viewModel.getInterval()!!).toLong()
                else
                    ((24*60*60*1000 - (firstTime - secondTime)) / viewModel.getInterval()!!).toLong()

            val calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = firstTimeStrings[0].toInt()
            calendar[Calendar.MINUTE] = firstTimeStrings[1].toInt()
            calendar[Calendar.SECOND] = 0
            Log.d("nott","CountDown $countDownInterval")
            Log.d("nott","Calendar ${calendar.timeInMillis}")
            Log.d("nott","Current ${System.currentTimeMillis()}")
            for (i in 1..viewModel.getInterval()!!) {
                setAlarm(calendar.timeInMillis + countDownInterval * i, i)
            }
        }
    }
}