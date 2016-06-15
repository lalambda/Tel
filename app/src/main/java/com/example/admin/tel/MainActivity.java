package com.example.admin.tel;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends Activity implements PhoneButton.OnClickListener {

    private PhoneButton num_1, num_2, num_3, num_4, num_5, num_6, num_7, num_8, num_9, num_0;
    private PhoneButton[] phoneButtons={num_0,num_1, num_2, num_3, num_4, num_5, num_6, num_7, num_8, num_9};
    private int[] id ={R.id.ring_btn_num_0,R.id.ring_btn_num_1,R.id.ring_btn_num_2,R.id.ring_btn_num_3
    ,R.id.ring_btn_num_4,R.id.ring_btn_num_5,R.id.ring_btn_num_6,R.id.ring_btn_num_7,R.id.ring_btn_num_8,R.id.ring_btn_num_9};
    private Button call;
    private TextView phoneNum;
    private StringBuffer sb = new StringBuffer();
    private Cursor cursor;
    private List<Contact> systemList;
    private List<Contact> contactsList;
    private List<Contact> contactListNumber;
    private List<Contact> bufferList;
    private ListView contactListView;
    private TableLayout tableLayout;
    private TextView nameTV;
    private TextView numberTV;
    private LinearLayout linearLayout;
    private ImageButton imageButton;
    private MyAdapter myAdapter;
    private int inputLength = -1;
    private boolean clickFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        phoneNum = (TextView) findViewById(R.id.ring_tv_phoneNum);
        for (int i=0;i<10;i++){
            phoneButtons[i] =(PhoneButton) findViewById(id[i]);
            phoneButtons[i].setOnClickListener(this);
        }
        linearLayout = (LinearLayout) findViewById(R.id.connect_call);
        nameTV = (TextView) findViewById(R.id.connect_name);
        numberTV = (TextView) findViewById(R.id.connext_tel);
        contactListView = (ListView) findViewById(R.id.content_list);
        imageButton = (ImageButton) findViewById(R.id.list_show_more);
        tableLayout = (TableLayout) findViewById(R.id.number_table);
        systemList = new ArrayList<>();
        contactsList = new ArrayList<>();
        contactListNumber = new ArrayList<>();
        myAdapter = new MyAdapter(this, R.layout.list_item, contactsList);
        bufferList = new ArrayList<>();
        View headView = View.inflate(this,R.layout.list_headview,null);
        contactListView.addHeaderView(headView);
        contactListView.setAdapter(myAdapter);
        //查询系统联系人
        cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            getContactPhone(cursor);
        }
        //号码键盘动画效果
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(100);
        set.addAnimation(animation);
        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 5.0f,
                Animation.ABSOLUTE, -10.0f   //设置回弹效果
        );
        animation.setDuration(200);
        set.addAnimation(animation);
        LayoutAnimationController controller
                = new LayoutAnimationController(set, 0.5f);
        tableLayout.setLayoutAnimation(controller);
        //显示更多按钮点击事件
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (clickFlag) {
                    AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                    alphaAnimation.setDuration(300);
                    TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -30.0f);
                    translateAnimation.setDuration(500);
                    AnimationSet animationSet = new AnimationSet(true);
                    animationSet.addAnimation(alphaAnimation);
                    animationSet.addAnimation(translateAnimation);
                    contactListView.setAnimation(animationSet);
                    animationSet.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            tableLayout.setVisibility(View.VISIBLE);
                            linearLayout.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    contactListView.setVisibility(View.GONE);
                } else {
                    tableLayout.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.INVISIBLE);
                    AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                    alphaAnimation.setDuration(300);
                    TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, -30.0f, 0);
                    translateAnimation.setDuration(500);
                    AnimationSet animationSet = new AnimationSet(true);
                    animationSet.addAnimation(alphaAnimation);
                    animationSet.addAnimation(translateAnimation);
                    contactListView.setAnimation(animationSet);
                    contactListView.setVisibility(View.VISIBLE);
                }
                clickFlag = !clickFlag;
            }
        });
        //号码显示监听
        phoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if (length == 0) {
                    imageButton.setVisibility(View.INVISIBLE);
                    nameTV.setText("");
                    numberTV.setText("");
                    tableLayout.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                    contactListView.setVisibility(View.GONE);
                    clickFlag = false;
                    contactsList.clear();
                    contactListNumber.clear();
                    return;
                }
                if (length > 11) {
                    phoneNum.setTextSize(getResources().getDimension(R.dimen.textsize10));
                } else if (length > 5) {
                    phoneNum.setTextSize(getResources().getDimension(R.dimen.textsize20));
                } else {
                    phoneNum.setTextSize(getResources().getDimension(R.dimen.textsize50));
                }
                search(s.toString());
                SequenceList sequenceList = new SequenceList();
                if (contactListNumber.size() > 0) {
                    Collections.sort(contactListNumber, sequenceList);
                }
                if (contactsList.size() > 0) {
                    Collections.sort(contactsList, sequenceList);
                }
                contactsList.addAll(contactListNumber);
                if (contactsList.size() > 0) {
                    imageButton.setVisibility(View.VISIBLE);
                    SpannableStringBuilder stringBuilder;
                    int spanPosition = contactsList.get(0).getPosition();
                    if (contactsList.get(0).isName()) {
                        stringBuilder = new SpannableStringBuilder(contactsList.get(0).getName().toString());
                        spanPosition = contactsList.get(0).getPosition();
                        stringBuilder.setSpan(new ForegroundColorSpan(Color.RED), spanPosition % 1000, spanPosition % 1000 + 1 + spanPosition / 1000, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        nameTV.setText(stringBuilder);
                        numberTV.setText(contactsList.get(0).getNumber());
                    } else {
                        stringBuilder = new SpannableStringBuilder(contactsList.get(0).getNumber().toString());
                        stringBuilder.setSpan(new ForegroundColorSpan(Color.RED), spanPosition % 1000, spanPosition % 1000 + 1 + spanPosition / 1000, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        nameTV.setText(contactsList.get(0).getName());
                        numberTV.setText(stringBuilder);

                    }
                } else {
                    imageButton.setVisibility(View.INVISIBLE);
                    nameTV.setText("");
                    numberTV.setText("");
                    return;
                }
                myAdapter.notifyDataSetChanged();
                bufferList.clear();
                for (Contact contact : contactsList) {
                    if (!bufferList.contains(contact)) {
                        bufferList.add(contact);
                    }
                }
            }
        });

    }

    private void search(String s) {
        int sLength = s.length();
        contactsList.clear();
        contactListNumber.clear();
        if (inputLength == -1 || sLength == 1 || inputLength > sLength) {  //根据传入值判断是上次查询的缓存还是查询所有数据
            query(systemList, s);
        } else {
            query(bufferList, s);
        }
        inputLength = sLength;
    }

    //根据传入数据判断是否只查询号码
    private void query(List<Contact> list, String s) {
        if (bufferList != null)
            if (s.contains("0") || s.contains("1")) {
                queryNumber(list, s);
            } else {
                queryAll(list, s);
            }
    }

    //查询号码和名字
    private void queryAll(List<Contact> list, String s) {
        for (Contact contacts : list) {
            int location;
            location = Utils.nameSearch(contacts.getSpellHead(), s);
            if (location >= 0) {
                location = location + (s.length() - 1) * 1000;
                contacts.setIsName(true);
                contacts.setPosition(location);
                contacts.setNumber(contacts.getMobileNumber());
                contactsList.add(contacts);
                searchNumber(contacts.getWorkNumber(), contacts, s);
                searchNumber(contacts.getTelephone(), contacts, s);
            } else {
                location = Utils.nameSearch(contacts.getSpellAll(), s);
                if (location >= 0) {
                    contacts.setIsName(true);
                    contacts.setPosition(location);
                    contacts.setNumber(contacts.getMobileNumber());
                    contactsList.add(contacts);
                    searchNumber(contacts.getWorkNumber(), contacts, s);
                    searchNumber(contacts.getTelephone(), contacts, s);
                } else {
                    searchNumber(contacts.getMobileNumber(), contacts, s);
                    searchNumber(contacts.getWorkNumber(), contacts, s);
                    searchNumber(contacts.getTelephone(), contacts, s);
                }
            }


        }
    }


    //查询号码
    private void queryNumber(List<Contact> list, String s) {
        for (Contact contact : list) {
            searchNumber(contact.getMobileNumber(), contact, s);
            searchNumber(contact.getWorkNumber(), contact, s);
            searchNumber(contact.getTelephone(), contact, s);
        }
    }

    private void searchNumber(String number, Contact contacts, String s) {
        int location = number.indexOf(s);
        if (location >= 0) {
            location = location + 1000 * (s.length() - 1);
            Contact contact = new Contact(contacts);
            contact.setIsName(false);
            contact.setPosition(location);
            contact.setNumber(number);
            contactListNumber.add(contact);
        }
    }

    //获取系统联系人数据
    private void getContactPhone(Cursor cursor) {
        int phoneColumn = cursor
                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(phoneColumn);
        if (phoneNum > 0) {
            // 获得联系人的ID号
            Contact contact = new Contact();
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            contact.setName(name);
//            String pinyinAll = Utils.converterToSpell(name);
            PinYinName pinYinName = Utils.converterToSpell(name.toLowerCase());
            contact.setSpellHead(Utils.spellToStringArray(pinYinName.getSpellHead()));
            contact.setSpellAll(pinYinName.getSpellAll());
            // 获得联系人电话的cursor
            Cursor phone = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
                            + contactId, null, null);
            while (phone.moveToNext()) {
                int index = phone
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int typeindex = phone
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                int phone_type = phone.getInt(typeindex);
                String phoneNumber = phone.getString(index).replace(" ", "");
                switch (phone_type) {
                    case 1://home
                        contact.setTelephone(phoneNumber);
                        break;
                    case 2://mobile
                        contact.setMobileNumber(phoneNumber);
                        break;
                    case 3://work
                        contact.setWorkNumber(phoneNumber);
                        break;
                }
            }
            systemList.add(contact);
        }
    }

    @Override
    public void onClick(View v) {
        String number = ((PhoneButton) v).getNumText();
        sb.append(number);
        phoneNum.setText(sb);
    }

    public void delete(View v) {
        int i = sb.length();
        if (i < 1)
            return;
        sb.deleteCharAt(i - 1);
        phoneNum.setText(sb);
    }


    /**
     * 对Contact进行排序
     */
    class SequenceList implements Comparator<Contact> {

        @Override
        public int compare(Contact lhs, Contact rhs) {
            int i = lhs.getPosition();
            int j = rhs.getPosition();
            if (i == j) {
                return lhs.getName().compareTo(rhs.getName());
            } else if (i < j) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
