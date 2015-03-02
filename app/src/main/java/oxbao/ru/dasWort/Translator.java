package oxbao.ru.dasWort;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

/**
 * Created by pocheptsov on 02.03.2015.
 */
public class Translator
{
    private static final String CLIENT_ID = "dasWortTranslate";
    private static final String CLIENT_SECRET = "oA7/CYhAjAqXh2NWkjEuJGPvGrTFD6oEnkq4+kqK7sY=";

    private Handler m_handler;

    public Translator(Handler ownHandler)
    {
        m_handler = ownHandler;
    }

    public void translate(final LanguageEnum type, String translate)
    {

        final String finalTranslate = translate;
        if (finalTranslate.equals("") || finalTranslate.equals(null))
        {
            return;
        }
        Thread t = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Translate.setClientId(CLIENT_ID);
                Translate.setClientSecret(CLIENT_SECRET);
                threadMsg("start");
                try
                {
                    String transtatedText = "*";
                    if (type == LanguageEnum.ON_RUS)
                    {
                        transtatedText = Translate.execute(finalTranslate, Language.GERMAN, Language.RUSSIAN);
                    }
                    if (type == LanguageEnum.ON_ENG)
                    {
                        transtatedText = Translate.execute(finalTranslate, Language.RUSSIAN, Language.GERMAN);
                    }
                    threadMsg(TypeMessageEnum.TEXT_MESSAGE, transtatedText, type);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                //threadMsg("Stop");
            }

            private void threadMsg(TypeMessageEnum typeMessage,String translatedText, LanguageEnum type)
            {
                if (!translatedText.equals(null) && !translatedText.equals(""))
                {
                    Message msgObj = m_handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString(EnumKeyMessages.TYPE_MESSAGE.toString(), typeMessage.toString());
                    b.putString(EnumKeyMessages.TRANSLATED_TEXT.toString(), translatedText);
                    b.putString(EnumKeyMessages.LANGUAGE_TYPE.toString(), type.toString());
                    msgObj.setData(b);
                    m_handler.sendMessage(msgObj);
                }
            }

            private void threadMsg(String msg)
            {
                Message msgObj = m_handler.obtainMessage();
                Bundle b = new Bundle();
                b.putString(EnumKeyMessages.TYPE_MESSAGE.toString(), TypeMessageEnum.STAFF_MESSAGE.toString());
                b.putString(EnumKeyMessages.);
                msgObj.setData(b);
                m_handler.sendMessage(msgObj);
            }
        });
        t.start();
       // pb_wait.setVisibility(View.VISIBLE);
    }
}
